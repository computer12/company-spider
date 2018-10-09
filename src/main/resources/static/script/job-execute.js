const execute$table = $("#job_execute_table");

function execute_init() {
    load_execute();
}

function load_execute() {
    execute$table.bootstrapTable({
        ajax: function (request) {
            $.ajax({
                type: "GET",
                url: "spiderJob/execute/get",
                success: function (msg) {
                    request.success({row: msg});
                    execute$table.bootstrapTable('load', msg.message);
                },
                error: function () {
                    alert("错误");
                }
            });
        },
        toolbar: '#job-execute-toolbar',
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
            field: 'targetWeb',
            title: '目标网站',
            switchable: true,
            formatter: targetWebFormatter
        }, {
            field: 'startTime',
            title: '启动时间',
            switchable: true,
            sortable: true
        }, {
            field: 'endTime',
            title: '结束时间',
            switchable: true,
            sortable: true
        }, {
            field: 'dataNum',
            title: '获得信息数',
            switchable: true
        }, {
            field: 'runStatus',
            title: '执行状态',
            switchable: true,
            formatter: runStatusFormatter
        }, {
            field: 'endMessage',
            title: '执行信息',
            switchable: true,
        }]
    });
    
}

function runStatusFormatter(v){
    if (v ===0)
        return '运行中...';
    if (v ===1)
        return '已完成';
    return '异常';
}