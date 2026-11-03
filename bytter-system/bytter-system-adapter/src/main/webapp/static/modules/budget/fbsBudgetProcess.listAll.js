var datatable;
bt.use(['form','datatable','corp','dateType','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox:true,
        order: [[5,'desc']],
        ajax: {
            'url': basePath+"/budget/fbsBudgetProcess/datatable?flag=queryall"
        },
        buttons: [{
            hide: bt.isnotResource('fbs:processQuery:fbsBudgetProcess:apply'),
            text: bt.lang('fbs.button.release'),
            action: function ( e, dt, node, config ) {
                var data = dt.rows('.selected').data();
                if(data.length < 1){
                    layer.msg(bt.lang('common.message.confirm.selectData'));
                    return;
                }
                var ids="";
                var rowData = data.splice(0,data.length);
                for(var i=0;i<rowData.length;i++){
                    if (ids == "") {
                        ids += rowData[i].id;
                    } else {
                        ids += "," + rowData[i].id;
                    }
                }
                parent.layer.confirm(bt.lang('common.message.confirm.release'),function(){
                    $.post(basePath + "/budget/fbsBudgetProcess/batchRelease/",{ids:ids}, function(result){
                        if(result.success){
                            datatable.draw();
                        }
                        // location.reload();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    },'JSON');
                });
            }
        }]
        ,columns: [
            {data:"corpId",bSortable:true,render: function ( data, type, full, meta ) {
                    return full.corpName;
                }
            },
            {data:"itemCode",bSortable:true},
            {data:"itemName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"processMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.processMoney);
                }
            },
            {data:"processDate",bSortable:true,className:'center'},
            {data:"billNo",bSortable:true,className:'center'},
            {data:"createDate",bSortable:true,className:'center'},
            {data:"status",bSortable:true,className:'center',
            	render: function ( data, type, full, meta ) {
                    return full.statusName;
                }
            },
            {data:"rmk",bSortable:true},
            {data:"businessName",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("fbsBudgetProcess.businessName",full.businessName);
                }
            },
            {data:"processSysname",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("fbsBudgetProcess.processSysname",full.processSysname);
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'curName',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum:'4',
                    dataName:'processMoney'
                }
            ]
        }
    });
});
