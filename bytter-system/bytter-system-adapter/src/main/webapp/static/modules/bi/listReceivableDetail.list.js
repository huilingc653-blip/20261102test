var datatable;
var dateType;
bt.use(['form','datatable','dateType','selectType','dicts','corp','page','moneyType','mulselect','money','rateType'],function(){
    $=bt.jquery,form=bt.form();
    bt.corp.init();
    bt.selectType.init();
    dicts=bt.dicts.init();
    bt.mulselect.init();
    var rate = bt.rateType.getFun();
    dateType= bt.dateType.init();
    moneyType=bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false,
        query: "search",
        checkbox:false,
        order: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bi/fcAccInterest/getDetail"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"netName",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true,className:'center'},
            {data:"blnctrAcc",bSortable:true,className:'center'},
            {data:"accName",bSortable:true,className:'center'},
            {data:"accType",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("InnerAccount.AccType",full.accType);
                }
            },
            {data:"curName",bSortable:true,className:'center'},
            {data:"accStartDate",bSortable:true,className:'center'},
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"interestEndDate",bSortable:true,className:'center'},
            {data:"dayNum",bSortable:true,className:'center'},
            {data:"interestRate",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return rate.format(full.interestRate);
                }
            },
            {data:"interest",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.interest);
                }
            },
            {data:"interestSign",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("Balance.InterestSign",full.interestSign);
                }
            },
            {data:"predrawSign",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("Admin.PredrawSign",full.predrawSign);
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: false,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '12',
                    dataName: 'interest'
                }
            ]
        }
    });
});