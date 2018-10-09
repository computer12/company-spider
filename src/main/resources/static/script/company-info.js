const company$table = $("#companyInfo_table");

function company_init() {
    load_company();
}

function load_company() {
    company$table.bootstrapTable('destroy').bootstrapTable({
        ajax: function (request) {
            $.ajax({
                type: "GET",
                url: "companyInfo/get",
                success: function (msg) {
                    request.success({row: msg});
                    company$table.bootstrapTable('load', msg.message);
                },
                error: function () {
                    alert("错误");
                }
            });
        },
        toolbar: '#company-toolbar',
        singleSelect: true,
        clickToSelect: true,
        sortName: "endTime",
        sortOrder: "desc",
        pageSize: 10,
        pageNumber: 1,
        pageList: "[10]",
        showRefresh: true,
        showColumns: true,
        search: true,
        pagination: true,
        columns: [{
            field: 'name',
            title: '企业名称',
            switchable: true
        }, {
            field: 'targetWeb',
            title: '数据来源',
            switchable: true,
            formatter: targetWebFormatter
        }, {
            field: 'registNumber',
            title: '工商注册号',
            switchable: true
        }, {
            field: 'updateTime',
            title: '更新时间',
            switchable: true,
            formatter: updateTimeFormatter
        }, {
            field: 'orgCode',
            title: '组织机构代码',
            switchable: true
        }, {
            field: 'industry',
            title: '行业',
            switchable: true,
        }, {
            field: 'creditCode',
            title: '统一信用代码',
            switchable: true,
        }, {
            field: 'ratepayerIdentifyNumber',
            title: '纳税人识别号',
            switchable: true,
        }]
    });
    
}

function updateTimeFormatter(v){
    return v.substring(0,10);
}