var billTable={};
var dataFun;
bt.use(['form','select','dicts','dateType','moneyType','corp','element','datatable','mulselect','date'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.mulselect.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    dateFun = bt.dateType.getFun();

    //查询列表时间初始化
    var start_balDate=dateFun.addDay(dateFun.get($("#end_balDate").val()),"-1");
    $("#start_balDate").val(dateFun.format(start_balDate,"yyyy-MM-dd"));

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/balCompare/datatable"

        },
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true},
            {data:"bal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.bal);
                }
            },
            {data:"befMonthDifBal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.befMonthDifBal);
                }
            },
            {data:"befMonthProportion",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.befMonthProportion);
                    return null;
                }
            },
            {data:"befQuarterBal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	//return bt.money.format(full.befQuarterBal);
                    return null;
                }
            },
            {data:"befQuarterDifBal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.befQuarterDifBal);
                    return null;
                }
            },
            {data:"befQuarterProportion",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                   // return bt.money.format(full.befQuarterProportion);
                    return null;
                }
            },
            {data:"befThreeMonthBal",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                	//return bt.money.format(full.befThreeMonthBal);
                    return null;
                }
            },
            {data:"befThreeMDifBal",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.befThreeMDifBal);
                    return null;
                }
            },
            {data:"befThreeMProportion",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.befThreeMProportion);
                    return null;
                }
            },
            {data:"befYearBal",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                   // return bt.money.format(full.befYearBal);
                    return null;
                }
            },
            {data:"befYearDifBal",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.befYearDifBal);.
                    return null;
                }
            },
            {data:"befYearProportion",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.befYearProportion);
                    return null;
                }
            },
            {data:"befYearProportion",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    // return bt.money.format(full.befYearProportion);
                    return null;
                }
            },
            {data:"yearEarlyDifBal",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    // return bt.money.format(full.yearEarlyDifBal);
                    return null;
                }
            },
            {data:"yearEarlyProportion",bSortable:true,className:'textRight',//
                render: function ( data, type, full, meta ) {
                    //return bt.money.format(full.yearEarlyProportion);
                    return null;
                }
            }
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                 {
                     colNum:'2',
                     dataName:'bal'
                 },{
                     colNum:'3',
                     dataName:'befMonthBal'
                 }
                 /*,{
                     colNum:'4',
                     dataName:'befMonthDifBal'
                 },{
                     colNum:'5',
                     dataName:'befMonthProportion'
                 },{
                     colNum:'6',
                     dataName:'befQuarterBal'
                 },{
                     colNum:'7',
                     dataName:'befQuarterDifBal'
                 },{
                     colNum:'8',
                     dataName:'befQuarterProportion'
                 },{
                     colNum:'9',
                     dataName:'befThreeMonthBal'
                 },{
                     colNum:'10',
                     dataName:'befThreeMDifBal'
                 },{
                    colNum:'11',
                    dataName:'befThreeMProportion'
                },{
                    colNum:'12',
                    dataName:'befYearBal'
                },{
                    colNum:'13',
                    dataName:'befYearDifBal'
                },{
                    colNum:'14',
                    dataName:'befYearProportion'
                },{
                    colNum:'15',
                    dataName:'yearEarlyBal'
                },{
                    colNum:'16',
                    dataName:'yearEarlyDifBal'
                },{
                    colNum:'17',
                    dataName:'yearEarlyProportion'
                }*/
            ]
        }
    });

    initDate();

    bt.selectDate = function(data) {
        var myDate=new Date(data)
        var month = myDate.getMonth()+1;
        var year = myDate.getFullYear();
        var newDay = layui.laydate.getEndDate(month,year);
        var newDate = year +"-"+ month  + "-" + newDay;
        dateType.get("monthDate").val(newDate);
    }

});

function initDate() {
    var date = new Date();
    var year = date.getFullYear()
    var month = date.getMonth()+1;
    var newDay = layui.laydate.getEndDate(month,year);
    var update = year +"-"+ month + "-" + newDay;

    bt.dateType.init().get("statisticsMonth").val(update);
}


//重置
function setReset(){
    setTimeout(function(){
    var start_balDate=dateFun.addDay(dateFun.get($("#end_balDate").val()),"-1");
    $("#start_balDate").val(dateFun.format(start_balDate,"yyyy-MM-dd"));
    },100);
}
