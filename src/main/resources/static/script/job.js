const job$table = $('#job_table');

let areaData; // 省级列表，不变的
let areaCityData; // 市级列表，依据选中省
let areaCountyData; // 区县列表，依据选中市

let default_province = 'zj'; // 浙江
let default_city = 'hangzhou';// 杭州
let default_county = '330104';// 江干区

const b_province$select = $('#baseUrl_province_tyc');
const b_city$select = $('#baseUrl_city_tyc');
const b_county$select = $('#baseUrl_county_tyc');

function getAreaChildren(province, city) {
    let citys = [];
    areaData.forEach(
        e => {
            if (e.base === province) {
                citys = e.children;
            }
        }
    );
    if (city !== undefined) {
        let counties = [];
        citys.forEach(
            e => {
                if (e.base === city) {
                    counties = e.children;
                }
            }
        );
        return counties;
    }
    return citys;
}

function job_init() {
    load_job();

    $.getJSON("data/area.json", data => {
        areaData = data;
        data.forEach(
            p => {
                b_province$select.append(new Option(p.name, p.base));
            }
        );
        b_province$select.selectpicker('refresh');
        b_province$select.on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
            if(clickedIndex !== undefined){
                let reflashData = areaData[clickedIndex].children;
                baseUrl_city_refresh_tyc(reflashData);
                //b_city$select.selectpicker('val', areaData[clickedIndex].children[0].base);
                b_city$select.selectpicker('val', '');
            }else{
                baseUrl_city_refresh_tyc(getAreaChildren(default_province));
                b_city$select.selectpicker('val', default_city);
            }
        });

        b_city$select.on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
            if (clickedIndex !== undefined) {
                let province = b_province$select.selectpicker('val');
                let city  = b_city$select.selectpicker('val');
                let reflushData = getAreaChildren(province,city);
                baseUrl_county_refresh_tyc(reflushData);
                b_county$select.selectpicker('val', "");
            }else{
                baseUrl_county_refresh_tyc(getAreaChildren(default_province,default_city));
                b_county$select.selectpicker('val', default_county);
            }
        });
        //baseUrl_tyc_reset();
    });

    // 激活属相控制
    $('#jobTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        // 获取已激活的标签页的名称
        let activeTab = $(e.target).data('target-web');
        // 获取前一个激活的标签页的名称
        // var previousTab = $(e.relatedTarget).data('target-web');
        job_save_reset();
    });
    $('#jobAddModal').on('hidden.bs.modal', function () {
        // 重置
        job_save_reset();
    });
}

function baseUrl_tyc_reset() {
    b_province$select.selectpicker('val', default_province);

}

function baseUrl_city_refresh_tyc(data) {
    areaCityData = data;
    b_city$select.html('');
    b_city$select.append(new Option('全部', ''));
    data.forEach(
        p => {
            b_city$select.append(new Option(p.name, p.base));
        }
    );
    b_city$select.selectpicker('refresh');
}

function baseUrl_county_refresh_tyc(data) {
    b_county$select.html('');
    b_county$select.append(new Option('全部', ''));
    data.forEach(
        p => {
            b_county$select.append(new Option(p.name, p.areaCode));
        }
    );
    b_county$select.selectpicker('refresh');
}

function jobSave() {
    let data = {};
    data.targetWeb = $('#jobTab li.active a[data-toggle="tab"]').data('target-web');

    data.startTime = $('#job_start_time').selectpicker('val');
    data.intervalTime = $('#job_interval_time').selectpicker('val');


    let job_id = $('#job_id').val();

    if (data.targetWeb === 'TYC') {
        data.dataNum = $('#job_data_num_tyc').selectpicker('val');
        data.eOrder = $('#job_order_tyc').selectpicker('val');

        let province = b_province$select.selectpicker('val');
        let city = b_city$select.selectpicker('val');
        let county = b_county$select.selectpicker('val');

        let baseUrl = 'https://www.tianyancha.com/search{order}/p{page}?base=';

        baseUrl += (city ? city :province);
        baseUrl += (county ? ('&areaCode='+county):'');

        console.log(baseUrl);
        data.baseUrl = baseUrl;

    } else if (data.targetWeb === 'QCC') {
        data.dataNum = $('#job_data_num_qcc').selectpicker('val');
        data.baseUrl = $('input[name="job_targetWeb"]:checked').val();
    }

    let url = job_id ? (data.id = $('#job_id').val() , "spiderJob/post") : "spiderJob/put";

    $.post(url, data, (data, status) => {
        if (data.retCode === 1) {
            my_message('保存成功');
            job$table.bootstrapTable('refresh');

            $('#jobAddModal').modal('hide');
        }
    });
}

function job_save_reset() {
    $('input[name="job_targetWeb"]')[0].checked = true;
    $('#job_data_num_qcc').selectpicker('val', $("#job_data_num_qcc")[0][0].text);
    $('#job_data_num_tyc').selectpicker('val', $("#job_data_num_tyc")[0][0].text);
    $('#job_start_time').selectpicker('val', $("#job_start_time")[0][0].text);
    $('#job_interval_time').selectpicker('val', $("#job_interval_time")[0][0].text);
    $('#job_id').val('');

    baseUrl_tyc_reset();
}

function job_save_table_to1(index) {
    $('#jobTab li:eq(' + index + ') a').tab('show');
}

load_job = function () {
    job$table.bootstrapTable({
        ajax: function (request) {
            $.ajax({
                type: "GET",
                url: "spiderJob/get",
                success: function (msg) {
                    request.success({row: msg});
                    job$table.bootstrapTable('load', msg.message);
                },
                error: function () {
                    alert("错误");
                }
            });
        },
        toolbar: '#job-toolbar',
        singleSelect: true,
        clickToSelect: true,
        sortName: "insertTime",
        sortOrder: "desc",
        pageSize: 10,
        pageNumber: 1,
        pageList: "[10]",
        showRefresh: true,
        showColumns: true,
        search: true,
        pagination: true,
        columns: [{
            field: 'targetWeb',
            title: '目标网站',
            switchable: true,
            formatter: targetWebFormatter
        }, {
            field: 'intervalTime',
            title: '间隔时间',
            switchable: true
        }, {
            field: 'startTime',
            title: '启动时间',
            switchable: true
        }, {
            field: 'dataNum',
            title: '目标数据',
            switchable: true
        }, {
            field: 'eorder',
            title: '排序规则',
            hidden: true,
            switchable: true
        }, {
            field: 'insertTime',
            title: '创建时间',
            switchable: true,
            sortable: true
        }, {
            title: '操作',
            field: 'id',
            align: 'center',
            events: window.operateJobEvents,
            formatter: job_genderOpt
        }]
    });
};

window.operateJobEvents = {
    'click .opt_e': function (e, value, row, index) {

        if (row.targetWeb === 'TYC') {
            job_save_table_to1(0);
            $('#job_data_num_tyc').selectpicker('val', row.dataNum);
            $('#job_order_tyc').selectpicker('val', row.eorder);

        } else {
            job_save_table_to1(1);
            $('input[name="job_targetWeb"][value=\'' + row.baseUrl + '\']')[0].checked = true;
            $('#job_data_num_qcc').selectpicker('val', row.dataNum);
        }
        $('#job_start_time').selectpicker('val', row.startTime);
        $('#job_interval_time').selectpicker('val', row.intervalTime);
        $('#job_id').val(row.id);

        $('#jobAddModal').modal('show');

    },
};

function job_genderOpt(v) {
    return [
        '<button class="btn btn-info btn-xs opt_e" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-pencil"></i>',
        '</button>',
        '<button class="btn btn-danger btn-xs opt_d" onclick="job_delete(' + v + ')" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</button>']
        .join('');
}

function job_delete(id) {
    $.post("spiderJob/delete/" + id,
        (data, status) => {
            job$table.bootstrapTable('remove', {field: 'id', values: [id]});
        }
    );
}