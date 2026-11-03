var selectType;
bt.use(['form','datatable','corp','moneyType','dateType','selectType','dicts', 'rateType','mulselect'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
	bt.mulselect.init();
    bt.corp.init();//单位
    bt.moneyType.init();//金额
    var dateTypes = bt.dateType.init();//日期
    selectType = bt.selectType.init();//下拉选择
    rateTypes = bt.rateType.init();//利率
    var dicts = bt.dicts.init();//字典
    //生成列表

    var height = $(window).height();
    var corpTableHeight = height - 210;
    if (corpTableHeight < 400){
        corpTableHeight = 400;
    }
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        customDom:'Brt',//自定义dom布局
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        scrollY: corpTableHeight,
        checkbox:false,
        exportFile: {
            extendExcelSingleRowInfoCallback: function () {
                var queryDate = $("#calcDate").val();
                var unitShowInfo = bt.lang('table.nisAllocationReport.curType');
                var curIdElement = selectType.get("curId");
                var curName = '';
                if(curIdElement){
                    var selectedCur = selectType.get("curId").getData();
                    if(selectedCur){
                        curName = selectedCur.text;
                    }
                }
                return [bt.lang('cms.report.end') + queryDate, '', '', '', '', '', '', bt.lang('table.cmsProvideLoanInfo.curId') + '：' +curName,, unitShowInfo];
            },
        },
        ajax: {
            'url': basePath+"/bi/cms/report/provide/capitalizeTable"
        },
        columns: [
            {data:"corpId",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                return full.corpName;
            }},
            {data:"bankTypeName",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                if(!bt.empty(data)){
                    return data;
                }
                return '';
            }},
            {data:"bankName",bSortable:false,className:"center"},
            {data:"plClassName",bSortable:false,className:"center"},
            {data:"leftMoney",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                return bt.money.format(full.leftMoney);
            }},
            {data:"bailMoney",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                var bondMoney = "";
                if(!bt.empty(data)){
                    bondMoney = bt.money.format(data);
                }else {
                    bondMoney = bt.money.format(0);
                }
                return bondMoney;
            }},
            {data:"converMoney",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                return bt.money.format(full.converMoney);
            }},
            {data:"plRate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                if(data){
                    return bt.rateType.getFun().format(data,'percent',4,',');
                }
                return "";
            }},
            {data:"sendDate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                if(!bt.empty(data)){
                    return bt.date.format(data, "yyyy-MM-dd");
                }
                return '';
            }},
            {data:"repayDate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                if(!bt.empty(data)){
                    return bt.date.format(data, "yyyy-MM-dd");
                }
                return '';
            }},
            {data:"guaMoney",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                return bt.money.format(full.guaMoney);
            }},
            {data:"guaWay",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                return data;
            }},
            {data:"contractCode",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                return data;
            }},
            {data:"startDate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                    if(!bt.empty(data)){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"endDate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                if(!bt.empty(data)){
                    return bt.date.format(data, "yyyy-MM-dd");
                }
                return '';
            }},
            {data:"guaOtherRmk",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                return "";
            }},
            {data:"rmk",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                return data;
            }},
            {data:"guaOtherDate",bSortable:false,className:"center"},
            {data:"rationBegindate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                    if(!bt.empty(data)){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"rationEnddate",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                    if(!bt.empty(data)){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"rationMoney",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.rationMoney);
                }}
        ]
    });

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == "null"){
            return true;
        }
        return false;
    }
});

