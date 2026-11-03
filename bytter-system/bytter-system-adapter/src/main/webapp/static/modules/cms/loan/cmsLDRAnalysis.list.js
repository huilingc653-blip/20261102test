var datatable,selectPre,dateType,selectType,sortType=0,dicts,firstCol="corpName",columns,headFistCol,searchDate;//统计维度默认是融资单位 headFistCol:表头第一列显示名称
bt.use(['form','datatable','btdate','corp','moneyType','rateType','dateType','selectType','date','bankType','dicts','mulselect','datatable2'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateType=bt.dateType.init();
    selectType=bt.selectType.init();
    bt.bankType.init();
    bt.mulselect.init();
    selectPre=new Array();//用于存放之前选择的对比周期
    selectPre=['M','S','L','Y'];//初始化全选
    dicts = bt.dicts.init();
    headFistCol=bt.lang('table.report.corpId');//表头第一列名称默认---单位
    searchDate=bt.date.format($("#searchEndDate").val(),'yyyy-MM-dd');
    //校验本位币
    form.verify({
        isNatualCur: function(value, item){ //value：表单的值、item：表单的DOM对象
            var isToCurr = dicts.get("isConvertStandCur").val();//是否折合本位币的值
            if(!value && isToCurr == "1"){
                return bt.lang("table.report.isNatualCur");
            }
        }
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    var firstCol;//第一列显示内容
    if(sortType==1){//统计维度是融资银行
        firstCol="bankTypeName";//银行类型名称
    }else{
        firstCol="corpName";//单位名称
    }
    setColumns(firstCol);//设置列加载数据
    initTable("#appTable",columns);//初始化表格
    // setColumnShow(0,true);//显示列--单位名称
    // setColumnShow(1,false);//隐藏列--银行类型名称

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    $("#endDate").keypress(function (e) {//监听回车事件
        if (e.which == 13) {
            changeSearchDate();
        }
    });

    changeCurSelect();
});
function setColumns(firstCol) {
    var dataObject;
    if(sortType==1){//统计维度是融资银行
        dataObject={
            data:firstCol,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html= '<a class="" href="#" data-row="'+meta.row+'" onclick="viewBankDtl(this)">'+full.bankTypeName+'</a>';
                return html;
            }
        };
    }else{//统计维度是融资单位
        dataObject={data:"corpName",className:"center",bSortable:true};
    }
    columns = [
        dataObject,
        {data:"totalDepositsD",className:"textRight",bSortable:false,//截止查询日期存款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalDepositsD);
            }
        },
        {data:"totalLoansD",className:"textRight",bSortable:false,//截止查询日期贷款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalLoansD);
            }
        },
        {data:"depositLoanRateD",bSortable:true,className:'center',//到截止日期存贷比:如果到截止查询日期贷款额度为0，则占比显示--
            render: function ( data, type, full, meta ) {
                if(full.totalLoansD==moneyFun.format(0)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.depositLoanRateD,'percent',2);
                }
            }
        },
        {data:"totalDepositsM",className:"textRight",bSortable:false,//上月存款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalDepositsM);
            }
        },
        {data:"totalLoansM",className:"textRight",bSortable:false,//上月贷款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalLoansM);
            }
        },
        {data:"depositLoanRateM",bSortable:true,className:'center',//较上月存贷比:如果上季度末贷款额度为0，则占比显示--
            render: function ( data, type, full, meta ) {
                if(full.totalLoansM==moneyFun.format(0)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.depositLoanRateM,'percent',2);
                }
            }
        },
        {data:"totalDepositsS",className:"textRight",bSortable:false,//上季度末存款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalDepositsS);
            }
        },
        {data:"totalLoansS",className:"textRight",bSortable:false,//上季度末贷款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalLoansS);
            }
        },
        {data:"depositLoanRateS",bSortable:true,className:'center',//较上季度末存贷比:如果上季度末贷款额度为0，则占比显示--
            render: function ( data, type, full, meta ) {
                if(full.totalLoansS==moneyFun.format(0)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.depositLoanRateS,'percent',2);
                }
            }
        },
        {data:"totalDepositsL",className:"textRight",bSortable:false,//去年同期存款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalDepositsL);
            }
        },
        {data:"totalLoansL",className:"textRight",bSortable:false,//去年同期贷款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalLoansL);
            }
        },
        {data:"depositLoanRateL",bSortable:true,className:'center',//较去年同期存贷比:如果去年同期贷款额度为0，则占比显示--
            render: function ( data, type, full, meta ) {
                if(full.totalLoansL==moneyFun.format(0)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.depositLoanRateL,'percent',2);
                }
            }
        },
        {data:"totalDepositsY",className:"textRight",bSortable:false,//年初贷款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalDepositsY);
            }
        },
        {data:"totalLoansY",className:"textRight",bSortable:false,//年初贷款总额
            render: function (data, type, full, meta) {
                return moneyFun.format(full.totalLoansY);
            }
        },
        {data:"depositLoanRateY",bSortable:true,className:'center',//较年初存贷比:如果较年初贷款额度为0，则占比显示--
            render: function ( data, type, full, meta ) {
                if(full.totalLoansY==moneyFun.format(0)){
                    return "--"
                }else{
                    return bt.rateType.getFun().format(full.depositLoanRateY,'percent',2);
                }
            }
        }
    ];
}
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
            'url': basePath+"/cms/report/cmsLDRanalysis/datatable"
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
        summary:{
            pageSum:true,
            format:{
                decimal: 2
            },
            sumList:[
                {
                    colNum:'1',
                    dataName:'totalDepositsD'
                },
                {
                    colNum:'2',
                    dataName:'totalLoansD'
                },
                {
                    colNum:'4',
                    dataName:'totalDepositsM'
                },
                {
                    colNum:'5',
                    dataName:'totalLoansM'
                },
                {
                    colNum:'7',
                    dataName:'totalDepositsS'
                },
                {
                    colNum:'8',
                    dataName:'totalLoansS'
                },
                {
                    colNum:'10',
                    dataName:'totalDepositsL'
                },
                {
                    colNum:'11',
                    dataName:'totalLoansL'
                },
                {
                    colNum:'13',
                    dataName:'totalDepositsY'
                },
                {
                    colNum:'14',
                    dataName:'totalLoansY'
                }
            ]
        },
        exportFile:{
            customPrint:{
                breakword:true,
                header:function(){
                    var html = '<tr>' +
                        '<th rowspan="2" style="text-align: center;">'+headFistCol+'</th>'+//单位或银行类型
                        '<th colspan="3" style="text-align: center;">'+bt.lang('cms.report.end')+searchDate+bt.lang('cms.loan.ternUnit.day')+'</th>'+//币别
                        '<th colspan="3" style="text-align: center;">'+bt.lang('statisticsReport.IntegratedQuery.befMonthEnd')+'</th>'+//较上月末
                        '<th colspan="3" style="text-align: center;">'+bt.lang('statisticsReport.IntegratedQuery.befQuarterEnd')+'</th>'+//较上季度末
                        '<th colspan="3" style="text-align: center;">'+bt.lang('statisticsReport.IntegratedQuery.befYear')+'</th>'+//较去年同期
                        '<th colspan="3" style="text-align: center;">'+bt.lang('statisticsReport.IntegratedQuery.yearEarly')+'</th>'+//较年初
                        '</tr>' ;
                    html+='<tr>'+
                        //较上月末
                        '<th style="text-align: center;">'+bt.lang('table.report.totalDeposits')+'</th>'+//存款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.totalLoans')+'</th>'+//贷款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.depositLoanRate')+'</th>'+//存贷比
                        //较上月末
                        '<th style="text-align: center;">'+bt.lang('table.report.totalDeposits')+'</th>'+//存款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.totalLoans')+'</th>'+//贷款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.depositLoanRate')+'</th>'+//存贷比
                        //较上季度末
                        '<th style="text-align: center;">'+bt.lang('table.report.totalDeposits')+'</th>'+//存款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.totalLoans')+'</th>'+//贷款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.depositLoanRate')+'</th>'+//存贷比
                        //较去年同期
                        '<th style="text-align: center;">'+bt.lang('table.report.totalDeposits')+'</th>'+//存款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.totalLoans')+'</th>'+//贷款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.depositLoanRate')+'</th>'+//存贷比
                        //较年初
                        '<th style="text-align: center;">'+bt.lang('table.report.totalDeposits')+'</th>'+//存款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.totalLoans')+'</th>'+//贷款合计
                        '<th style="text-align: center;">'+bt.lang('table.report.depositLoanRate')+'</th>'+//存贷比
                        '</tr>' ;
                    return html;
                }
            }
        }
    });
}

bt.changeCycle = function(data) {
    var selectNew=new Array();//选中的最新数据
    // var selectChange =new Array();//改变前后变化的数据
    $.each(data,function(i){
        selectNew[i]=data[i].value;
    });

    selectChange=getDif(selectNew,selectPre);
    function getDif(a,b){
        var array1=a.length>=b.length?a:b;
        var array2=a.length>=b.length?b:a;
        return array1.concat(array2).filter(function(v, i, arr) {
            return arr.indexOf(v) === arr.lastIndexOf(v);
        });
    }

    if(selectPre.length>data.length){//选择减少
        $.each(selectChange,function(i){
            var cycleValue=selectChange[i];
            if(!bt.empty(cycleValue)){
                setColHide(cycleValue);
            }
        });
    }else if(selectPre.length<data.length){//选择增多
        $.each(data,function(i){
            var cycleValue=selectChange[i];
            if(!bt.empty(cycleValue)){
                setColShow(cycleValue);
            }
        });
    }
    selectPre.splice(0,selectPre.length);
    selectPre=selectNew;
//
}
function setColumnShow(index,flag) {
    var column = datatable.column( index );
    column.visible( flag );
}

function setColShow(cycle) {
    if(cycle=="M"){//较上月
        setColumnShow(4,true);//显示列
        setColumnShow(5,true);//显示列
        setColumnShow(6,true);//显示列
    }
    if(cycle=="S"){//较上季末
        setColumnShow(7,true);//显示列
        setColumnShow(8,true);//显示列
        setColumnShow(9,true);//显示列
    }
    if(cycle=="L"){//较去年同期
        setColumnShow(10,true);//显示列
        setColumnShow(11,true);//显示列
        setColumnShow(12,true);//显示列
    }
    if(cycle=="Y"){//较去年同期
        setColumnShow(13,true);//显示列
        setColumnShow(14,true);//显示列
        setColumnShow(15,true);//显示列
    }
    $(".totalDeposits"+cycle+"").show();//显示对应合计列
    $(".totalLoans"+cycle+"").show();
    $(".totalLoans"+cycle+"").next().show();
}

function setColHide(cycle) {
    if(cycle=="M"){
        setColumnShow(4,false);//隐藏列
        setColumnShow(5,false);//隐藏列
        setColumnShow(6,false);//隐藏列
    }
    if(cycle=="S"){
        setColumnShow(7,false);//隐藏列
        setColumnShow(8,false);//隐藏列
        setColumnShow(9,false);//隐藏列
    }
    if(cycle=="L"){
        setColumnShow(10,false);//隐藏列
        setColumnShow(11,false);//隐藏列
        setColumnShow(12,false);//隐藏列
    }
    if(cycle=="Y"){
        setColumnShow(13,false);//隐藏列
        setColumnShow(14,false);//隐藏列
        setColumnShow(15,false);//隐藏列
    }
    $(".totalDeposits"+cycle+"").hide();//隐藏对应合计列
    $(".totalLoans"+cycle+"").hide();
    $(".totalLoans"+cycle+"").next().hide();
}

function setSortType() {
    datatable.destroy();
    sortType=dicts.get("typesOfStatistics").val();
    if(sortType=="1"){//统计维度是融资银行
        selectType.get("bankType").val({"disabled":false});//设置银行类型可选
        firstCol="bankTypeName";//银行类型名称
        headFistCol=bt.lang("btBankType.typeName");//银行类型
        $("#bankTable").show();
        $("#appTable").hide();
        setColumns(firstCol);
        initTable("#bankTable",columns);
    }else{//统计维度是融资单位
        selectType.get("bankType").val({"disabled":true});//设置银行类型不可选
        firstCol="corpName";//单位名称
        headFistCol=bt.lang("table.report.corpId");//单位
        $("#bankTable").hide();
        $("#appTable").show();
        setColumns(firstCol);
        initTable("#appTable",columns);
    }
    form.render('select');
}

//查看银行类型明细
function viewBankDtl(el){
    var data = layui.jquery("#bankTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param = new Object();
    var cycle="";
    param["bankType"] = data.bankType;
    param["endDate"] =dateType.get("endDate").val();
    param["curId"] = selectType.get("curId").val();
    param["isContain"] = dicts.get("isContain").val();
    param["typesOfStatistics"] = dicts.get("typesOfStatistics").val();
    //param["cycleList"] = dicts.get("cycle").val();
    param["isConvertStandCur"] = dicts.get("isConvertStandCur").val();
    param["moneyUnit"] = dicts.get("moneyUnit").val();
    var cycleArr= new Array();
    cycleArr=dicts.get("cycle").val();
    if(cycleArr.length>0){
        $.each(cycleArr,function(i){
            if(i==cycleArr.length-1){
                cycle+=cycleArr[i];
            }else{
                cycle+=cycleArr[i]+",";
            }
        });
    }
    param["cycle"] = cycle;
    if(!bt.empty(data.netId)){
        param["netId"] = data.netId;
    }
    param=parseParam(param);
    if(data){
        if(bt.navtab){//增加国际化
            bt.navtab.addNavtab(bt.lang('accountGrant.detailSearch'), basePath + "/cms/report/cmsLDRanalysis/viewBankTypeDtl?" +param);
            return;
        }
    }
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

//控制币别是否可选
function changeCurSelect() {
    var isConvertStandCur=dicts.get("isConvertStandCur").val();
    // var curId=selectType.get("curId").val();
    if(isConvertStandCur=="1"){//折合时，
        selectType.get("convertCurId").attr({"data-disabled":false,"data-required":true});
        selectType.get("curId").attr({"data-value": "","data-disabled":true,"data-required":false})
    }else{//不折合
        selectType.get("convertCurId").attr({"data-value": "","data-disabled":true,"data-required":false});
        selectType.get("curId").attr({"data-value": "","data-disabled":false,"data-required":true})
    }
    form.renderIcon();
    checkConvertCurCallBack();
}

function changeSearchDate() {
    var  endDate=dateType.get("endDate").val();
    searchDate=bt.date.format(endDate,'yyyy-MM-dd');
    $(".searchEndDate").html("");
    var html=bt.lang('cms.report.end')+searchDate+bt.lang('cms.loan.ternUnit.day');
    $("#searchEndDate1").html(html);
    $("#searchEndDate2").html(html);
    checkConvertCurCallBack();
}

function resetCallback() {
    window.location.reload();
}


/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var convertCurId = selectType.get("convertCurId").val();
    var date = dateType.get("endDate").val();
    if (isNotEmpty(convertCurId)) {
/*        $.ajax({
            url: basePath + "/cms/report/cmsLDRanalysis/checkConvertCurId",
            type:"post",
            data:{
                "origCurId": origCur,
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


