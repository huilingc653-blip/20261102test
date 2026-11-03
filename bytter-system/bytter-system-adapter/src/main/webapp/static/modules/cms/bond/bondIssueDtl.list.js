bt.use(['form','datatable','btdate','dateType','selectType','corp'], function(){
    var dateType = bt.dateType.init();
    bt.selectType.init()
    bt.corp.init()
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        order: [[7,'asc']],
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/bond/issue/issueDtlDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"bondIssueNum",bSortable:true},
            {data:"leadUnderwriter",bSortable:true},
            {data:"batchNo",bSortable:true},
            {data:"issueMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.issueMoney);
                }
            },
            {data:"rmbIssueTotal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.rmbIssueTotal);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"dueDate",bSortable:true},
            {data:"issueStartDate",bSortable:true},
            {data:"issueEndDate",bSortable:true},
            {data:"issuePrice",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.issuePrice);
                }
            },
            {data:"issueRmk",bSortable:true},
            {data:"statusName",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.status == 95){
                        return "审批通过";
                    } else {
                        return bt.dict("AB_BILL_EMITBILL_STATUS",full.status);
                    }
                }
            }
        ]
    });
});