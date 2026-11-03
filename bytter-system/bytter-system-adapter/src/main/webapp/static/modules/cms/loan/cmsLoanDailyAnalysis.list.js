var datatable,dateTypes,selectType,dicts,columns,sortType=0,headFistCol,corp,averageTh,tableId="#appTable";//sortType:统计维度默认是融资单位 headFistCol:表头第一列显示名称
bt.use(['form','datatable','btdate','corp','moneyType','rateType','dateType','selectType','date','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    corp=bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateTypes=bt.dateType.init();
    selectType=bt.selectType.init();
    dicts = bt.dicts.init();
    headFistCol=bt.lang('table.fbsBudget.corpId');//表头第一列名称默认---单位
    averageTh=bt.lang('table.report.dailyAverage');//默认按天查询，显示‘日均’
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    setColumns();//设置列加载数据
    initTable(tableId,columns);//初始化表格:tableId="#appTable"

    // setDateMax();

    //查询开始时间改变事件
    bt.changeStartDate = function(data) {
        var searchFrequency=dicts.get("searchFrequency").val();
        var dateNow=$("#searchDateMax").val();//系统当前日期
        var startDate=data;
        var endDateMax;//计算的最大日期
        if(searchFrequency=="0"){//按天查询
            endDateMax=bt.date.format(bt.date.addDay(new Date(startDate),31),"yyyy-MM-dd");
        }else{//按月查询
            startDate=startDate+"-01";
            endDateMax=bt.date.addMonth(startDate,12);//往后推算12个月
            endDateMax=bt.date.format(bt.date.addDay(endDateMax,-1),"yyyy-MM");//减一天
        }
        var days=bt.date.getBetweenDay(endDateMax,dateNow);//对比计算出最大日期和当前日期
        if(days<=0){
            endDateMax=dateNow;
        }
        $('[data-biz-name="endDate"]').removeAttr("data-max");
        dateTypes.get("endDate").attr("data-max",endDateMax);
        dateTypes.render();
    }

    /**
     *
     * @param data
     */
    bt.changeEndDate = function(data) {
        checkConvertCurCallBack();
    }
    //校验本位币
    // form.verify({
    //     isNatualCur: function(value, item){ //value：表单的值、item：表单的DOM对象
    //         var isToCurr = dicts.get("isConvertStandCur").val();//是否折合本位币的值
    //         if(!value && isToCurr == "1"){
    //             return bt.lang("table.report.isNatualCur");
    //         }
    //     }
    // });
});

function setDateMax(){
    var dateNow=$("#searchDateMax").val();//系统当前日期
    dateTypes.get("startDate").attr("data-max",dateNow);
    dateTypes.get("endDate").attr("data-max",dateNow);
    dateTypes.render();
    dateTypes.get("startDate").val($("#searchSatrtMonth").val());
    dateTypes.get("endDate").val($("#searchEndMonth").val());
}

function setColumns() {
    var dataObject;
    if(sortType==1){//统计维度是融资银行
        dataObject={
            data:"bankTypeName",bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html= '<a class="" href="#" data-row="'+meta.row+'" onclick="viewDtl(this)">'+full.bankTypeName+'</a>';
                return html;
            }
        };
    }else{//统计维度按单位统计
        dataObject={
            data:"corpName",bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html= '<a class="" href="#" data-row="'+meta.row+'" onclick="viewDtl(this)">'+full.corpName+'</a>';
                return html;
            }
        };
    }
    columns = [
        dataObject,
        {data:"curName",className:"center",bSortable:false},//币别
        {data:"curLoanMoney",className:"textRight",bSortable:false,//期末余额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.curLoanMoney);
            }
        },
        {
            data:"curLoanDailyMoney",bSortable:false,className:'textRight',
            render: function (data, type, full, meta) {//本期日均贷款金额
                var html= '<a class="" href="#" data-row="'+meta.row+'" onclick="viewDailyBalanceDtl(this)">'+ moneyFun.format(full.curLoanDailyMoney)+'</a>';
                return html;
            }
        },
        {data:"preLoanDailyMoney",className:"textRight",bSortable:false,//上期日均贷款金额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.preLoanDailyMoney);
            }
        },
        {data:"preDifference",className:"textRight",bSortable:false,//较上期差额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.preDifference);
            }

        },
        {data:"preRate",bSortable:true,className:'textRight',//上期比例，若较上期_差额为0，显示为“-”
            render: function ( data, type, full, meta ) {
                if(full.preDifference==moneyFun.format(0) || isEmpty(data)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.preRate,'percent',2);
                }
            }
        },

        {data:"lastLoanDailyMoney",className:"textRight",bSortable:false,//上年同期日均贷款金额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.lastLoanDailyMoney);
            }

        },
        {data:"lastDifference",className:"textRight",bSortable:false,//较上年同期差额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.lastDifference);
            }

        },
        {data:"lastRate",bSortable:true,className:'textRight',//上年同期比例,若较上期_差额为0，显示为“-”
            render: function ( data, type, full, meta ) {
                if(full.lastDifference==moneyFun.format(0) || isEmpty(data)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.lastRate,'percent',2);
                }
            }
        }]
}
//生成列表
function initTable(id,columns){
    datatable = bt.datatable.init({
        elem: id,
        dom: 'Brtlip',
        colReorder: true,
        fixedColumns: {
            leftColumns:1,//代表冻结左边两列
            // rightColumns:1//代表冻结右边一列
        },
        isInitData: false,//初始化不加载数据，点击查询后加载
        order: [2,'desc'],
        ajax: {
            'url': basePath+"/cms/report/loanDailyAnalysis/datatable"
        },
        searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:columns,
        exportFile:{
            customPrint:{
                breakword:true,
                header:function(){
                    var html = '<tr>' +
                        '<th rowspan="2" style="text-align: center;">'+headFistCol+'</th>'+//单位或银行/结算中心
                        '<th rowspan="2" style="text-align: center;">'+bt.lang('cms.report.cur')+'</th>'+//币别
                        '<th colspan="2" style="text-align: center;">'+bt.lang('table.report.current')+'</th>'+//本期
                        '<th colspan="3" style="text-align: center;">'+bt.lang('table.report.prior')+'</th>'+//较上期
                        '<th colspan="3" style="text-align: center;">'+bt.lang('table.report.lastYear')+'</th>'+//较上年同期
                        '</tr>' ;
                    html+='<tr>'+
                        '<th style="text-align: center;">'+bt.lang('table.report.endingBalance')+'</th>'+//期末余额
                        '<th style="text-align: center;">'+averageTh+'</th>'+//日均/月均
                        '<th style="text-align: center;">'+averageTh+'</th>'+//日均/月均
                        '<th style="text-align: center;">'+bt.lang('table.report.difference')+'</th>'+//差额
                        '<th style="text-align: center;">'+bt.lang('table.report.ratio')+'</th>'+//比例
                        '<th style="text-align: center;">'+averageTh+'</th>'+//日均/月均
                        '<th style="text-align: center;">'+bt.lang('table.report.difference')+'</th>'+//差额
                        '<th style="text-align: center;">'+bt.lang('table.report.ratio')+'</th>'+//比例
                        '</tr>' ;
                    return html;
                }
            }
        }
    });
}
//控制币别是否可选
function changeCurSelect() {
    var isConvertStandCur=dicts.get("isConvertStandCur").val();
    if(isConvertStandCur=="1"){//折合本位币时，把币别设置成空，不可选
        selectType.get("convertCurId").attr({"data-value": "","data-disabled":false,"data-required":true});
        selectType.get("curId").attr({"data-value": "", "data-disabled": true,"data-required":false});
    }else{//不折合币种,设置币种必选
        selectType.get("convertCurId").attr({"data-value": "", "data-disabled": true,"data-required":false});
        selectType.get("curId").attr({"data-value": "", "data-disabled": false, "data-required": true});
    }
    form.renderIcon();
    checkConvertCurCallBack();
}



//币别改变事件
function changeCur(data) {
    $("#curCode").val(data.id);
    checkConvertCurCallBack();
}

//查询频率改变事件
function setSearchDate(){
    var searchFrequency=dicts.get("searchFrequency").val();
    var searchSatrtMonth=$("#searchSatrtMonth").val();
    var searchEndMonth=$("#searchEndMonth").val();
    var searchDateMax=$("#searchDateMax").val();
    var monthHtml='<input type="text" data-biz-name="startDate" name="startDate" class="layui-input bt-input-inline bt-biz-dateType" data-endid="endDate" data-type="month" data-callback="bt.changeStartDate" lay-verify="required" value="'+searchSatrtMonth+ '"data-max='+searchDateMax+'">\n' +
        '<span class="date-mid">~</span>\n' +
        '<input type="text"data-biz-name="endDate"  name="endDate" class="layui-input bt-input-inline bt-biz-dateType" id="endDate" data-type="month"  data-callback="bt.changeEndDate"  lay-verify="required" value="'+searchEndMonth+ '" data-max='+searchDateMax+'>';

    var dayHtml='<input type="text" data-biz-name="startDate" name="startDate" class="layui-input bt-input-inline bt-biz-dateType" data-endid="endDate" data-today="true" data-callback="bt.changeStartDate" lay-verify="required" data-max='+searchDateMax+'>\n' +
        '<span class="date-mid">~</span>\n' +
        '<input type="text"data-biz-name="endDate"  name="endDate" class="layui-input bt-input-inline bt-biz-dateType" id="endDate"  data-today="true" data-callback="bt.changeEndDate"  lay-verify="required" data-max='+searchDateMax+'>';
    $(".dateShowDiv").html();
    $(".averageTh").html();
    if(searchFrequency=="0"){//按天查询
        averageTh=bt.lang("table.report.dailyAverage");//日均
        $(".dateShowDiv").html(dayHtml);
    }else{//按月查询
        $(".dateShowDiv").html(monthHtml);
        averageTh=bt.lang("table.report.monthAverage");//月均
    }
    $(".averageTh").html(averageTh);
    bt.dateType.init();
    datatable.destroy();//销毁表格
    initTable(tableId,columns);//重新初始化表格
    dicts.get("isConvertStandCur").val("0");
    selectType.get("convertCurId").attr({"data-value": "", "data-disabled": true,"data-required":false});
    selectType.get("curId").attr({"data-value": "", "data-disabled": false, "data-required": true});
    form.renderIcon();
}
//统计维度改变事件
function setSortType() {
    datatable.destroy();
    sortType=dicts.get("typesOfStatistics").val();
    if(sortType=="1"){//统计维度是融资银行
        headFistCol=bt.lang('table.report.bankOrNet');//银行/结算中心
        $("#bankTable").show();
        $("#appTable").hide();
        setColumns();
        tableId="#bankTable";
    }else{//统计维度是融资单位
        headFistCol=bt.lang('table.fbsBudget.corpId');//单位
        $("#bankTable").hide();
        $("#appTable").show();
        setColumns();
        tableId="#appTable";
    }
    initTable(tableId,columns);
}
//查看某统计维度下各类贷款明细
function viewDtl(el){
    var data;
    if (sortType == "1") {//统计维度为贷款机构
        data = layui.jquery("#bankTable").DataTable().row(layui.jquery(el).data("row")).data();
    } else {//统计维度为单位
        data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    }
    var param = new Object();
    if(!bt.empty(data.corpId)){//单位id(按单位查询时)
        param["corpId"] = data.corpId;
    }else{//按贷款机构查询时传查询条件中的单位
        var corps=corp.get("corpId").val();
        if(!bt.empty(corps)){//设置查询条件中的单位
            param["searchCorps"] =corps;
        }
    }
    if(!bt.empty(data.bankType)){//银行类型
        param["bankType"] = data.bankType;
    }
    if(!bt.empty(data.netId)){//结算中心
        param["netId"] = data.netId;
    }
    var isConvertStandCur= dicts.get("isConvertStandCur").val();
    var curId=data.curId;
    param["isConvertStandCur"] =isConvertStandCur;
    param["curId"] = curId;
    param.convertCurId = selectType.get("convertCurId").val();
    setParams(param);
    param=parseParam(param);
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '550px'],
            maxmin: true,
            content: basePath + "/cms/report/loanDailyAnalysis/viewTypeOfStatisticsDtl?" +param
        });
    }
}


/**
 * 查看日均贷款明细
 * @param el
 */
function viewDailyBalanceDtl(el){
    var data;
    if (sortType == "1") {//统计维度为贷款机构
        data = layui.jquery("#bankTable").DataTable().row(layui.jquery(el).data("row")).data();
    } else {//统计维度为单位
        data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    }
    var param = new Object();
    if(!bt.empty(data.corpId)){//单位id(按单位查询时)
        param["corpId"] = data.corpId;
    }else{//按贷款机构查询时传查询条件中的单位
        var corps=corp.get("corpId").val();
        if(!bt.empty(corps)){//设置查询条件中的单位
            param["searchCorps"] =corps;
        }
    }
    if(!bt.empty(data.bankType)){//银行类型
        param["bankType"] = data.bankType;
    }
    if(!bt.empty(data.netId)){//结算中心
        param["netId"] = data.netId;
    }
    var isConvertStandCur= dicts.get("isConvertStandCur").val();
    var curId;
    if(isConvertStandCur=="1"){//折合本位币的时候取列表的币别
        curId=data.curId;
    }else{
        curId=selectType.get("curId").val();
    }
    param["isConvertStandCur"] =isConvertStandCur;
    param["curId"] = curId;
    param.convertCurId = selectType.get("convertCurId").val();
    setParams(param);
    param=parseParam(param);
    if(data){
        parent.layer.open({
            type: 2,
            area: ['900px', '550px'],
            maxmin: true,
            content: basePath + "/cms/report/loanDailyAnalysis/viewDailyBalance?" +param
        });
    }
}


function setParams(param) {
    param["startDate"] =dateTypes.get("startDate").val();
    param["endDate"] =dateTypes.get("endDate").val();
    param["isContain"] = dicts.get("isContain").val();
    param["typesOfStatistics"] = dicts.get("typesOfStatistics").val();
    param["searchFrequency"] = dicts.get("searchFrequency").val();
    param["moneyUnit"] = dicts.get("moneyUnit").val();
    param["loanSign"] = dicts.get("loanSign").val();
}


//json转url参数
var parseParam = function (param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function (i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};
//重置按钮回调函数
function resetCallback() {
    window.location.reload();
}


/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var convertCurId = selectType.get("convertCurId").val();
    var date = dateTypes.get("endDate").val();
    var searchFrequency = dicts.get("searchFrequency").val();
    if (isNotEmpty(convertCurId)) {
        /*$.ajax({
            url: basePath + "/cms/report/loanDailyAnalysis/checkConvertCurId",
            type:"post",
            data:{
                "origCurId": origCur,
                "convertCurId": convertCurId,
                "date": date,
                "searchFrequency": searchFrequency
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("convertCurId").val("");
                }
            }
        });*/
        $.ajax({
            url: basePath + "/report/deposit/balance/checkConvertCurId",
            type:"post",
            data:{
                "isAllCur":"1",
                "convertCurId": convertCurId,
                "date": date
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("convertCurId").val("");
                }
            }
        });
    }
}


/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}