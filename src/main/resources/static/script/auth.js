const auth$table = $("#auth_table");

function auth_init() {
    load_auth();

    $('#authAddModal').on('hidden.bs.modal', function () {
        authEditReset();
    });
    checkMark();
    $('#qcc_mark').on('closed.bs.alert', function () {
        removeMark("QCC");
    });
    $('#tyc_mark').on('closed.bs.alert', function () {
        removeMark("TYC");
    });
}
function removeMark(targetWeb) {
    $.get("spiderJob/mark/delete?targetWeb=" + targetWeb,
        (data, status) => {

        }
    );
}

function checkMark() {
    let error = false;
    $.get("spiderJob/mark/get",
        (data, status) => {
            data.message.forEach(
                e =>{
                    if(e.name === "QCC"){
                        $('#qcc_mark').removeClass("hide");
                        error = true;
                    }else if(e.name === "TYC"){
                        $('#tyc_mark').removeClass("hide");
                        error = true;
                    }
                }
            );
            if(error){
                $("#show_auth").click();
                window.location.hash = "about";
            }
        }
    );
}

function authEditReset() {
    $('#auth_id').val("");
    $('#authKey').val("");
    $('#authValue').val("");
    $('#targetWeb_tyc')[0].checked = true;
    $('#authType_cookie')[0].checked = true;
}

function load_auth() {
    auth$table.bootstrapTable({
        ajax: function (request) {
            $.ajax({
                type: "GET",
                url: "authConf/get",
                success: function (msg) {
                    request.success({row: msg});
                    auth$table.bootstrapTable('load', msg.message);
                },
                error: function () {
                    alert("错误");
                }
            });
        },
        toolbar: '#auth-toolbar',
        singleSelect: true,
        clickToSelect: true,
        sortName: "updateTime",
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
            field: 'authKey',
            title: '认证信息key',
            switchable: true
        }, {
            field: 'authValue',
            title: '认证信息value',
            switchable: true,
            formatter: authValueFormatter
        }, {
            field: 'updateTime',
            title: '更新时间',
            switchable: true,
            sortable: true
        }, {
            title: '操作',
            field: 'id',
            align: 'center',
            events: window.operateAuthEvents,
            formatter: auth_genderOpt
        }]
    });
}

window.operateAuthEvents = {
    'click .opt_e': function (e, value, row, index) {

        $('#auth_id').val(row.id);
        $('#authKey').val(row.authKey);
        $('#authValue').val(row.authValue);

        $('input[name="targetWeb"][value=' + row.targetWeb + ']')[0].checked = true;
        $('input[name="authType"][value=' + row.authType + ']')[0].checked = true;
        $('#authAddModal').modal('show');
    },
};

function auth_genderOpt(v) {
    return [
        '<button class="btn btn-info btn-xs opt_e" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-ok"></i>',
        '</button>',
        '<button class="btn btn-danger btn-xs opt_d" onclick="auth_delete(' + v + ')" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</button>']
        .join('');
}

function auth_delete(id) {
    $.post("authConf/delete/" + id,
        (data, status) => {
            auth$table.bootstrapTable('remove', {field: 'id', values: [id]});
        }
    );
}

// 配置新增方法
function authConfigSave() {
    let data = {};
    data.targetWeb = $('input[name="targetWeb"]:checked').val();
    data.authType = $('input[name="authType"]:checked').val();

    data.authKey = $('#authKey').val();
    data.authValue = $('#authValue').val();

    let auth_id = $('#auth_id').val();

    let handle = function (data, status) {
        if (data.retCode === 1) {
            my_message('保存成功');
            auth$table.bootstrapTable('refresh');

            $('#authAddModal').modal('hide')
        }
    };
    let url = auth_id ? (data.id = $('#auth_id').val() , "authConf/post") : "authConf/put";

    $.post(url, data, (data, status) => {
        if (data.retCode === 1) {
            my_message('保存成功');
            auth$table.bootstrapTable('refresh');

            $('#authAddModal').modal('hide')
        }
    });
}

function authValueFormatter(v) {
    let authValueShowLength = 25;
    let showValue = v;
    if (v.length > authValueShowLength) {
        showValue = v.substring(0, authValueShowLength - 3) + '...';
    }
    return showValue;
}