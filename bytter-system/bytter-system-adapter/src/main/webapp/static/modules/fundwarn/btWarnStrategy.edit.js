var dicts,dateType,warnDataTable,selectCorpsTable,selectBankAccInfoTable,selectAccInfoTable,moneyType,bankAccAtt,bankAccNature,isAttribute,btmulselect;
bt.use(['form','element','datatable','select','checkboxs','selectType','moneyType','corp','dicts','dateType','mulCorp','mulselect','mulBankAccInfo','mulAccInfo'], function() {
    var $ = bt.jquery, form = bt.form();
    var element = layui.element;
    var corps = bt.corp.init();
    moneyType = bt.moneyType.init();
    var allDictsByCheckboxs = $('[data-type="checkbox"]');
    bt.each(allDictsByCheckboxs,function(i,item){
        var dictGroup = $(item).data("group");
        var defaultvalue = $(item).data("defaultvalue")
        if(defaultvalue){
            var dicts = bt.dictContext[dictGroup];
            var isCheckall = true;
            for(var value in dicts){
                if(defaultvalue.toString().indexOf(value.toString()) == -1){
                    isCheckall = false;
                }
            }
            if(isCheckall){
                $(item).data("defaultvalue","all")
            }
        }
    })
    dicts = bt.dicts.init();
    dateType = bt.dateType.init();
    var select = bt.selectType.init();
    var mulCorp=bt.mulCorp.init();
    var mulBankAccInfo=bt.mulBankAccInfo.init();
    var mulAccInfo=bt.mulAccInfo.init();
    bt.mulselect.init();

    //单位生成列表
    selectCorpsTable = function(){
        //生成列表
        if(warnDataTable){
            $('#appTable').DataTable().destroy();
            $('#appTable').html('');
        }
        warnDataTable = bt.datatable.init({
            elem: "#appTable",
            ignoreButtons:['colvis','output','print'],
            buttonsLayout:false,
            colReorder: true,
            dom: 'Brtlip',
            scrollY:'200px',
            scrollX:true,
            scrollCollapse: true,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath+"/fundwarn/btwarnstrategy/showCorpByCorpIds"
            },
            columns: [
                {data:"netName",bSortable:true,title:bt.lang("table.sysCorp.netName")},
                {data:"name",bSortable:true,title:bt.lang("table.sysCorp.corpName")},
                {data:"code",bSortable:true,title:bt.lang("table.sysCorp.code")}
            ]
        });
    }
    //银行账户生成列表
    selectBankAccInfoTable = function() {
        //生成列表
        if (warnDataTable) {
            $('#appTable').DataTable().destroy();
            $('#appTable').html('');
        }
        warnDataTable = bt.datatable.init({
            elem: "#appTable",
            ignoreButtons: ['colvis', 'output', 'print'],
            buttonsLayout: false,
            colReorder: true,
            dom: 'Brtlip',
            scrollY: '200px',
            scrollX: true,
            scrollCollapse: true,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/fundwarn/btwarnstrategy/getNotDeleteAndValidAndBankAcc"
            },
            columns: [
                {data: "bankAcc", bSortable: true, className: "center",title:bt.lang("table.btBankAccApp.bankAcc")},
                {data: "accName", bSortable: true, className: "center",title:bt.lang("table.btBankAccApp.accName")},
                {data: "corpName", bSortable: true, className: "center",title:bt.lang("table.btInnerAccApp.corpId")},
                {data: "curName", bSortable: true, className: "center",title:bt.lang("table.ratBkrationRelief.curCode")}
            ]
        });
    }

    //内部账户生成列表
    selectAccInfoTable = function() {
        //生成列表
        if (warnDataTable) {
            $('#appTable').DataTable().destroy();
            $('#appTable').html('');
        }
        warnDataTable = bt.datatable.init({
            elem: "#appTable",
            ignoreButtons: ['colvis', 'output', 'print'],
            buttonsLayout: false,
            colReorder: true,
            dom: 'Brtlip',
            scrollY: '200px',
            scrollX: true,
            scrollCollapse: true,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/fundwarn/btwarnstrategy/getblnctrAccByAccAndcurId"
            },
            columns: [
                {data: "blnctrAcc", bSortable: true, className: "center", title: bt.lang("table.btInnerAccApp.innerAcc")},
                {data: "accName", bSortable: true, className: "center", title: bt.lang("table.btBankAccApp.accName")},
                {data: "netName", bSortable: true, className: "center", title: bt.lang("table.fbsBudget.net")},
                {data: "corpName", bSortable: true, className: "center", title: bt.lang("table.btInnerAccApp.corpId")},
                {data: "curName", bSortable: true, className: "center", title: bt.lang("table.ratBkrationRelief.curCode")},
                {data: "accTypeName", bSortable: true, className: "center", title: bt.lang("table.btInnerAccApp.accType")}
            ]
        });
    }




    $(document).ready(function () {
        $.ajax({
            url: basePath + "/basic/business/btcurrency/json",
            method: 'GET', dataType: 'json', async: false,
            success: function (data) {
                if (data) {
                    $("#curId").empty();

                    $.each(data, function (index) {
                        var curIdSelete = "<option value='" + data[index].id + "'"+ " data-curCode='" + data[index].curCode + "'" ;
                        if($("#curIdStr").val()==data[index].id){
                            curIdSelete=curIdSelete+" selected='selected' ";
                        }
                        curIdSelete=curIdSelete+">" + data[index].curName + "</option>";
                        $("#curId").append(curIdSelete);
                    });
                }
                form.render('select');
            }
        });
    });

    //单位选择调用控件后传递值
    bt.externalMulCorpCallback=function (data,dom) {
        var corpIdArr = [];
        var netIdArr = [];

        $.each(data,function (key, val) {
            corpIdArr.push(val.id);
            netIdArr.push(val.corpNetId);
        });

        // 数组去重函数
        function unique(arr){
            var res = [];
            for(var i=0;i<arr.length;i++){
                if(res.indexOf(arr[i]) === -1){
                    res.push(arr[i]);
                }
            }
            return res;
        }

        var corpIds = unique(corpIdArr).join(",");
        var netIds = unique(netIdArr).join(",");

        $("#netIdStr").val(netIds);
        $("#corpIdAndAccStr").val(corpIds);
        $("#corpAccSelectQuary").val("0");
        showAccTable(false);//刷新表格
    }
    //单位选择调用控件前传递netId
    bt.externalNetIdParams=function(){
        if($("#netIdStr").val()==""){
            layer.alert(bt.lang('btwarnstrategy.corpselect'), {icon: 7});
            return false;
        }else{
            mulCorp.set({
                "netId":$("#netIdStr").val()
            });
            return true;
        }
    }

    //银行账户选择调用控件后传递值
    bt.externalMulBankAccInfoCallback=function (data,dom) {
        var bankAcc;
        $.each(data,function (key, val) {
                if(key==0){
                    bankAcc=val.bankAcc;
                }else if(key>0){
                    bankAcc=bankAcc+","+val.bankAcc;
                }

            }
        )
        $("#corpIdAndAccStr").val(bankAcc);

        $("#corpAccSelectQuary").val("0");
        showAccTable(false);//刷新表格
    }
    //银行账户选择调用控件前传递netId
    bt.externalMulBankAccInfoNetIdParams=function(){
        if($("#netIdStr").val()==""){
            layer.alert(bt.lang('请先选择网点'), {icon: 7});
            return false;
        }else{
            mulBankAccInfo.set({
                "netId":$("#netIdStr").val(),
                "curId":$("#curId").val(),
                "resource":""
            });
            return true;
        }
    }

    //内部账户选择调用控件后传递值
    bt.externalMulAccInfoCallback=function (data,dom) {
        var blnctrAcc;
        $.each(data,function (key, val) {
                if(key==0){
                    blnctrAcc=val.blnctrAcc;
                }else if(key>0){
                    blnctrAcc=blnctrAcc+","+val.blnctrAcc;
                }

            }
        )
        $("#corpIdAndAccStr").val(blnctrAcc);
        $("#corpAccSelectQuary").val("0");
        showAccTable(false);//刷新表格
    }
    //内部账户选择调用控件前传递netId
    bt.externalMulAccInfoNetIdParams=function(){
        if($("#netIdStr").val()==""){
            layer.alert(bt.lang('请先选择网点'), {icon: 7});
            return false;
        }else {
            mulAccInfo.set({
                "netId": $("#netIdStr").val(),
                "curId":$("#curId").val(),
                "resource": ""
            });
            return true;
        }
    }
    //银行账号属性复选框
    bankAccAtt=bt.checkboxs.init({
        elem: ".bankAccAtt",
        name: "bankAccAttribute",
        valueField: "id",
        textField: "attributeName",
        url: basePath + "/bankacc/bisaccattribute/json",
        checkAll: true,
        checkedvalue:$("#bankAttStr").val(),
        change: function (data) {
            // console.log(data)
        }
    });
    //内部账号性质复选框
    bankAccNature=bt.checkboxs.init({
        elem: ".bankAccNature",
        name: "bankAccNature",
        valueField: "id",
        textField: "natureName",
        url: basePath + "/bankacc/btaccnature/json",
        checkAll: true,
        checkedvalue:$("#bankAccNatureStr").val(),
        change: function (data) {
            // console.log(data)
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        //bt.loading.show();
        var dataInfo=data.field;
        //预警类型
        var typeCode =$("select[name='typeCode']").val();
        //除系统外付款外,其他类别的预警条件值都必填
        if(typeCode!='outPay'){
            if($("#beginNum").val().length==0  || (dicts.get("endOperator").val() != "" && dicts.get("endOperator").val() != undefined && $("#endNum").val().length==0)){
                layer.alert(bt.lang('table.btwarnstrategy.condition.errorMessage'), {icon: 7});
                return false;
            }
        }
        //自定义选择时单位id或账号选择不能为空
        if(($("#corpIdAndAccStr").val()=="" || $("#corpIdAndAccStr").val()=="select" )&&typeCode!='tpBalOver'){
            //预警维度
            if($("select[name='dimensionCode']").val()=='corp'){
                layer.alert(bt.lang('table.btwarnstrategy.corpselect.errorMessage'), {icon: 7});
            }else{
                layer.alert(bt.lang('table.btwarnstrategy.accountselect.errorMessage'), {icon: 7});
            }
            return false;
        }
        if (typeCode == 'budgetProgress'){
            var timeType = dicts.get('timeType').val();
            if (isNull(timeType)){
                layer.alert(bt.lang('common.placeholder.select')  + bt.lang('table.fbsBudget.timeType'), {icon: 7});
                return false;
            }
        }
        //业务属性选择判断
        var bankAttStr="";
        var bankAccTypeStr="";
        var bankAccOnlineStr="";
        var bankAccNatureStr="";
        var subjectNameStr="";
        if(isAttribute=="bank"&&typeCode!='tpBalOver' ){
            if(dataInfo.bankAccAttribute==""){
                layer.alert(bt.lang('table.btwarnstrategy.bankAccAttribute.errorMessage'), {icon: 7});
                return false;
            }
            if(dataInfo.bankAccType==""){
                layer.alert(bt.lang('table.btwarnstrategy.bankAccType.errorMessage'), {icon: 7});
                return false;
            }
            if(dataInfo.bankAccOnline==""){
                layer.alert(bt.lang('table.btwarnstrategy.bankAccOnline.errorMessage'), {icon: 7});
                return false;
            }
            $('input[name="bankAccAttribute"]:checked').each(function () {
                if ($(this).val() != "all") {
                    bankAttStr += $(this).val() + ",";
                }
            });
            $('input[name="bankAccType"]:checked').each(function () {
                if ($(this).val() != "all") {
                    bankAccTypeStr += $(this).val() + ",";
                }
            });
            $('input[name="bankAccOnline"]:checked').each(function () {
                if ($(this).val() != "all") {
                    bankAccOnlineStr += $(this).val() + ",";
                }

            });
        }else if(isAttribute=="interior"&&typeCode!='tpBalOver'){
            if(dataInfo.bankAccNature==""){
                layer.alert(bt.lang('table.btwarnstrategy.bankAccNature.errorMessage'), {icon: 7});
                return false;
            }
            $('input[name="bankAccNature"]:checked').each(function () {
                if ($(this).val() != "all") {
                    bankAccNatureStr += $(this).val() + ",";
                }
            });

            if($("input[name='subjectRadio']:checked").val()=='0'){
                var allCorpNameStr = "";
                $.ajaxSettings.async = false;
                var dimensionCode =$("select[name='dimensionCode']").val();
                if(dimensionCode=='corp') {//单位
                    //获取网点下所有的单位
                    $.post(basePath + "/sys/corp/getCorpListByNetIds", {netIds:$("#netIdStr").val()}, function (data) {
                        if (data.success){
                            var allCorpList = data.obj;
                            if (allCorpList.length > 0){
                                for (var i = 0; i < allCorpList.length; i++) {
                                    allCorpNameStr += allCorpList[i]+","
                                }
                            }
                        }
                    },"json");
                }else if(dimensionCode == 'fcAcc'){
                    //获取网点下所有的内部账号
                    $.post(basePath + "/inneraccount/fcacc/getFcAccByNetIds", {netIds:$("#netIdStr").val()}, function (data) {
                        if (data.success){
                            var allFcAccList = data.obj;
                            if (allFcAccList.length > 0){
                                for (var i = 0; i < allFcAccList.length; i++) {
                                    allCorpNameStr += allFcAccList[i].blnctrAcc+","
                                }
                            }
                        }
                    },"json");
                }else if (dimensionCode == 'bankAcc') {
                    //获取网点下所有的银行账号
                    $.post(basePath + "/bankacc/btbankaccquery/getNotDeleteByNetIds", {netIds: $("#netIdStr").val()}, function (data) {
                        if (data.success) {
                            var allBankAccList = data.obj;
                            if (allBankAccList.length > 0) {
                                for (var i = 0; i < allBankAccList.length; i++) {
                                    allCorpNameStr += allBankAccList[i].bankAcc + ","
                                }
                            }
                        }
                    }, "json");
                }
                $.ajaxSettings.async = true;
            }else{
                if(dataInfo.subjectName==""){
                    layer.alert(bt.lang('table.btwarnstrategy.subjectName.errorMessage'), {icon: 7});
                    return false;
                }
                subjectNameStr=dataInfo.subjectName;
            }
        }
        if(typeCode!='outPay') {
            if (dicts.get("beginOperator").val() == "10" &&
                dicts.get("endOperator").val() == "20" &&
                Number($("#beginNum").val()) >= Number($("#endNum").val())) {
                layer.alert(bt.lang('table.btwarnstrategy.before.and.after.judgment'), {icon: 7});
                return false;
            }else if(dicts.get("beginOperator").val() == "20" &&
                dicts.get("endOperator").val() == "10" &&
                Number($("#beginNum").val()) <= Number($("#endNum").val())){
                layer.alert(bt.lang('table.btwarnstrategy.after.and.before.judgment'), {icon: 7});
                return false;
            }
        }
        dataInfo.bankAttStr=bankAttStr;
        dataInfo.bankAccTypeStr=bankAccTypeStr;
        dataInfo.bankAccOnlineStr=bankAccOnlineStr;
        dataInfo.bankAccNatureStr=bankAccNatureStr;
        dataInfo.subjectNameStr=subjectNameStr;

        var action = "/fundwarn/btwarnstrategy/modify";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return true;

    });

    changeView();
    subjectShow();
    stateInit();

    bt.dicts.init().get("endOperator").val(($("#endOperatorOld").val() == null || $("#endOperatorOld").val() == undefined) ? "" :  $("#endOperatorOld").val());
    bt.dicts.init().get("numOperator").val(($("#numOperatorOld").val() == null || $("#numOperatorOld").val() == undefined) ? "" :  $("#numOperatorOld").val());
    initConditionAssembly();


    if($("#corpAccSelectShow").val()==0){
        $("#appTableId").hide();
    }else{
        $("#appTableId").show();
    }

});



//改变状态 只有预警阶段为事中的才需要出来维护（类别：相同公司付款eqCorpPay、大额现金支出cashPay、大额账户支出bankAccPay）
function changeState(data) {
    dicts.get('dimensionCode').attr({'data-displayData':typecodeDimensions[data.value]});
    //预警类型
    var typeCode =$("select[name='typeCode']").val();
    //改变状态
    if( typeCode=='eqCorpPay' || typeCode=='cashPay' || typeCode=='bankAccPay' || typeCode=='daySumPay') {
        $("#statusView").show();
        dicts.get("status").val(1);
    }else{
        $("#statusView").hide();
        dicts.get("status").val(0);
    }
    changeView();
    // corpAccSelectCallback();
    /*



     */
}
//初始化状态 只有预警阶段为事中的才需要出来维护（类别：相同公司付款eqCorpPay、大额现金支出cashPay、大额账户支出bankAccPay）
function stateInit() {
    dicts.get('dimensionCode').attr({'data-displayData':typecodeDimensions[$("select[name='typeCode']").val()]});
    //预警类型
    var typeCode =$("select[name='typeCode']").val();
    //改变状态
    if( typeCode=='eqCorpPay' || typeCode=='cashPay' || typeCode=='bankAccPay' || typeCode=='daySumPay') {
        $("#statusView").show();
    }else{
        $("#statusView").hide();
    }
    if($("#corpAccSelectShow").val()==0){
        $("#mulCorpsDiv").hide();//单位选择
        $("#mulBankAccDiv").hide();//银行账户选择
        $("#mulAccDiv").hide();//内部账户选择
    }
}


/*
方法内容：预警类别和预警维度值变化时：
1.改变预警条件模块的条件头名称及备注显示隐藏状态
2.改变账号选择模块中的选择控件的显示及隐藏：
单位银行账户模式选择（余额超标、相同公司付款、净流入透支、账户余额持续下降、系统外付款、支出户流入）、
单位内部账户模式选择（库存现金超标、存贷比超标预警）、
银行账号选择（余额超标、大额账户支出、净流入透支、账户余额持续下降、系统外付款、支出户流入）、
内部账户（库存现金超标、大额现金支出）
3.改变业务属性选择的内容
*/
function changeView( isChangeNet ) {
    $("#platformDiv").addClass("ignore-required-hide")
    $("#merchantDiv").addClass("ignore-required-hide")
    //预警类型
    var typeCode =$("select[name='typeCode']").val();
    //预警维度
    var dimensionCode =$("select[name='dimensionCode']").val();
    $("#conditionTr").show();//预警条件模块
    $("#remarkTr").hide();//预警条件模块备注
    $("#operationattributeDiv").hide();//业务属性选择模块
    $("#mulCorpsDiv").hide();//单位选择
    $("#mulBankAccDiv").hide();//银行账户选择
    $("#mulAccDiv").hide();//内部账户选择
    $("#merchantDiv").hide();//商户号选择
    $("#platformDiv").hide()
    $("#accSelectH2").html(bt.lang('table.btwarnstrategy.accountselect.title'));//账号选择模块标题
    $("#bankAccDiv").hide();//单位银行账号业务属性Div
    $("#interiorAccDiv").hide();//单位内部账号业务属性Div
    $("#tpAccountDiv").hide();
    //预算执行参数配置
    $("#timeTypeSelectDiv").hide();
    $("#timeType").removeAttr("data-required","true");

    isAttribute="no";
    var newText = "";
    if( typeCode=='balOver' ) {//余额超标
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.sumbalance'));//条件模块条件头
            newText = '全部单位'
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.bankacc.balance'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='eqCorpPay'){//相同公司付款
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.dealmoney'));
            corpChangeShow();
        }
    }else if(typeCode=='bankAccPay'){//大额账户支出
        if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.dealmoney'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='daySumPay'){//单日累计支付
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.daySumPay.head'));
            corpChangeShow();
        }
    }else if(typeCode=='budgetProgress'){//预算执行进度
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.budgetProgress.head')+'%');
            $("#accSelectH2").html(bt.lang('table.btwarnstrategy.corpselect.title'));
            $("#remarkTr").show();
            $("#mulCorpsDiv").show();//单位选择
            $("#remarkTd").html(bt.lang('btwarnstrategy.condition.warn'));
            newText = '全部单位'
            $("#corpAccSelect").find('input:first-child').attr('title',newText);
            $($("#corpAccSelect").find('div')[0]).find('span').text(newText);
        }
        $("#timeTypeSelectDiv").show();
        $("#timeType").attr("data-required","true");
        bt.form().render();
    }else if(typeCode=='oldBankAcc'){//久悬户预警
        if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.oldBankAcc.head'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='inflowOver'){//净流入透支
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.fdigdp'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.bankacc.fdigdp'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='balDown'){//账户余额持续下降
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.endmonth.balancedecline'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.bankacc.endmonth.balancedecline'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='expendInflow'){//支出户流入
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpflowintomoneycollect'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.flowintomoney'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='cashOver'){//库存现金超标
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#accSelectH2").html(bt.lang('table.btwarnstrategy.corpselect.title'));
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpinterioracc.balancecollect'));
            $("#mulCorpsDiv").show();//单位选择
            $("#operationattributeDiv").show();//业务属性选择模块
            $("#interiorAccDiv").show();//单位内部账号业务属性Div
            isAttribute="interior";
        }else if(dimensionCode=='fcAcc'){//内部账户
            newText = '全部内部账号'
            $("#mulAccDiv").show();//内部账户选择
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.interioracc.balance'));
        }
    }else if(typeCode=='depositLoanOver'){//存贷比超标
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#accSelectH2").html(bt.lang('table.btwarnstrategy.corpselect.title'));
            $("#remarkTr").show();
            $("#mulCorpsDiv").show();//单位选择
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.loantodeposit.exceedwarning')+'%');
            $("#remarkTd").html(bt.lang('btwarnstrategy.condition.warn1')+'\n\r<br/>' +
                '                '+bt.lang('btwarnstrategy.condition.warn2'));
        }
    }else if(typeCode=='cashPay'){//大额现金支出
        if(dimensionCode=='fcAcc'){//内部账户
            newText = '全部内部账号'
            $("#mulAccDiv").show();//内部账户选择
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.interioracc.dealbalance'));
        }
    }else if(typeCode=='outPay'){//系统外付款
        $("#conditionTr").hide();
        if(dimensionCode=='corp'){//单位
            newText = '全部单位'
            $("#remarkTr").show();
            $("#remarkTd").html(bt.lang('condition.btwarnstrategy.corpbankacc.sysouterpayment.message'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            newText = '全部银行账号'
            $("#remarkTr").show();
            $("#remarkTd").html(bt.lang('condition.btwarnstrategy.bankacc.sysouterpayment.message'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='tpBalOver'){//三方账户余额预警
        $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.sumbalance'));//条件模块条件头
        newText = '全部单位'
        // corpChangeShow();
        $("#accSelectH2").html(bt.lang('单位选择'));//账号选择模块标题

        $("#mulCorpsDiv").show();//单位选择
        if(dimensionCode=='dsPlatform'){//平台
            if( btmulselect&&btmulselect.get("merchantNo")){
                btmulselect.get("merchantNo").val('');
            }

            $("#platformDiv").removeClass("ignore-required-hide")
            $("#warnConditionHead").html(bt.lang('tp.ds.balance')); //账户余额
            $("#tpAccountDiv").show();//三方账户选择模块
            $("#platformDiv").show();//平台选择
            $("#tpAccSelectH2").html(bt.lang('tp.ds.platformselect.title')); //平台选择
            $("#selectName").html(bt.lang('tp.ds.platformselect.title')); //平台选择
            newText = '全部单位'
            $("#corpAccSelect").find('input:first-child').attr('title',newText);
            $($("#corpAccSelect").find('div')[0]).find('span').text(newText);
        }else if(dimensionCode=='dsMerchant'){//商户号
            if( dicts&&dicts.get("platformCode")){
                dicts.get("platformCode").val('');
            }

            $("#merchantDiv").removeClass("ignore-required-hide")
            $("#warnConditionHead").html(bt.lang('tp.ds.balance')); //账户余额
            $("#tpAccountDiv").show();//三方账户选择模块
            $("#merchantDiv").show();//商户号选择
            $("#tpAccSelectH2").html(bt.lang('tp.ds.merchantselect.title')); //商户号选择
            $("#selectName").html(bt.lang('tp.ds.merchantselect.title')); //商户号选择
            newText = '全部单位'
            $("#corpAccSelect").find('input:first-child').attr('title',newText);
            $($("#corpAccSelect").find('div')[0]).find('span').text(newText);
            btmulselect=bt.mulselect.init()
        }
        // $("#mulCorpsDiv").show();//单位选择
        // if(dimensionCode=='dsPlatform'){//平台
        //     $("#warnConditionHead").html(bt.lang('tp.ds.balance')); //账户余额
        //     $("#tpAccountDiv").show();//三方账户选择模块
        //     $("#platformDiv").show();//平台选择
        //     $("#tpAccSelectH2").html(bt.lang('tp.ds.platformselect.title')); //平台选择
        //     $("#selectName").html(bt.lang('tp.ds.platformselect.title')); //平台选择
        //     newText = '全部单位'
        // }else if(dimensionCode=='dsMerchant'){//商户号
        //     $("#warnConditionHead").html(bt.lang('tp.ds.balance')); //账户余额
        //     $("#tpAccountDiv").show();//三方账户选择模块
        //     $("#merchantDiv").show();//商户号选择 $("#platformDiv").show()
        //     $("#tpAccSelectH2").html(bt.lang('tp.ds.merchantselect.title')); //商户号选择
        //     $("#selectName").html(bt.lang('tp.ds.merchantselect.title')); //商户号选择
        //     newText = '全部单位'
        //     btmulselect=bt.mulselect.init()
        // }
    }
    $("#corpAccSelect").find('input:first-child').attr('title',newText);
    $($("#corpAccSelect").find('div')[0]).find('span').text(newText);
    if(typeCode=='oldBankAcc'){//久悬户预警
        $("#forwardTimeId").show();
        bt.moneyType.init().get("forwardTime").attr("lay-verify","required|strlength")
        bt.moneyType.init().get("forwardTime").val($("#forwardTimeOld").val())
    } else {
        $("#forwardTimeId").hide();
        bt.moneyType.init().get("forwardTime").attr("lay-verify","strlength")
        bt.moneyType.init().get("forwardTime").val("")
    }
    showAccTable( isChangeNet );
}

//单位银行账户模式（余额超标、相同公司付款、净流入透支、账户余额持续下降、系统外付款、支出户流入）
function corpChangeShow(){
    $("#accSelectH2").html(bt.lang('table.btwarnstrategy.corpselect.title'));//单位选择模块标题
    $("#mulCorpsDiv").show();//单位选择
    $("#operationattributeDiv").show();//业务属性选择模块
    $("#bankAccDiv").show();//单位银行账号业务属性Div
    isAttribute="bank";
}


//网点下拉框回调
function netChange(obj) {
    showAccTable(false);//刷新表格
    var netIdString = "";
    if (obj.length > 0){
        for (var i = 0; i < obj.length; i++) {

            if (netIdString != "" || netIdString != undefined){
                netIdString += obj[i].id;
                netIdString += ",";
            }else {
                netIdString = netIdString + "," + obj[i].id;
            }
        }
    }
    if (netIdString != "" || netIdString != undefined){
        if (netIdString.substring(netIdString.length-1,netIdString.length) == ',' ){
            $("#netIdStr").val(netIdString.substring(0, netIdString.length-1));
        }else {
            $("#netIdStr").val(netIdString);
        }

    }
    // $("#netIdStr").val(obj.id);
    corpAccSelectCallback(true);
}

function corpAccSelectCallback( arg1 ) {

    var isChangeNet = typeof arg1 === 'boolean'?true:false;

    if($("#netIdStr").val()==""){
        layer.alert(bt.lang('请先选择网点'), {icon: 7});
    }else {
        if($("input[name='corpAccSelect']:checked").val()==0){
            var allCorpNameStr = "";
            $.ajaxSettings.async = false;
            var dimensionCode =$("select[name='dimensionCode']").val();
            if(dimensionCode=='corp') {//单位
                //获取网点下所有的单位
                $.post(basePath + "/sys/corp/getCorpListByNetIds", {netIds:$("#netIdStr").val()}, function (data) {
                    if (data.success){
                        var allCorpList = data.obj;
                        if (allCorpList.length > 0){
                            for (var i = 0; i < allCorpList.length; i++) {
                                allCorpNameStr += allCorpList[i]+","
                            }
                        }
                    }
                },"json");
            }else if(dimensionCode == 'fcAcc'){
                //获取网点下所有的内部账号
                $.post(basePath + "/inneraccount/fcacc/getFcAccByNetIds", {netIds:$("#netIdStr").val()}, function (data) {
                    if (data.success){
                        var allCorpList = data.obj;
                        if (allCorpList.length > 0){
                            for (var i = 0; i < allCorpList.length; i++) {
                                allCorpNameStr += allCorpList[i].blnctrAcc+","
                            }
                        }
                    }
                },"json");
            }else if (dimensionCode == 'bankAcc') {
                //获取网点下所有的银行账号
                $.post(basePath + "/bankacc/btbankaccquery/getNotDeleteByNetIds", {netIds: $("#netIdStr").val()}, function (data) {
                    if (data.success) {
                        var allCorpList = data.obj;
                        if (allCorpList.length > 0) {
                            for (var i = 0; i < allCorpList.length; i++) {
                                allCorpNameStr += allCorpList[i].bankAcc + ","
                            }
                        }
                    }
                }, "json");
            }
            $.ajaxSettings.async = true;
            allCorpNameStr=allCorpNameStr?allCorpNameStr:'all';
            $("#corpIdAndAccStr").val(allCorpNameStr);
            $("#mulCorpsDiv").hide();//单位选择
            $("#mulBankAccDiv").hide();//银行账户选择
            $("#mulAccDiv").hide();//内部账户选择
            $("#corpAccSelectQuary").val("0");
            $("#appTableId").hide();
            showAccTable(false);
        }else{
            $("#appTableId").show();
            $("#corpAccSelectQuary").val("1");
            showAccTable();
            $("#corpIdAndAccStr").val("select");
            $("#mulCorpsDiv").hide();//单位选择
            changeView(isChangeNet);
        }
    }
}

//单位、银行账户、内部账户统计主体筛选列表显示
function showAccTable(isChangeNet){
    //预警类型
    var typeCode =$("select[name='typeCode']").val();
    //预警维度
    var dimensionCode =$("select[name='dimensionCode']").val();
    if(typeCode && dimensionCode
        && $.trim(typeCode) && $.trim(dimensionCode)){
        if(dimensionCode=='corp'){//单位
            !isChangeNet && selectCorpsTable();
        }else if(dimensionCode=='bankAcc'){//银行账号
            !isChangeNet && selectBankAccInfoTable();
        }else if(dimensionCode=='fcAcc') {//内部账户
            !isChangeNet && selectAccInfoTable();
        }
        else if(dimensionCode=='dsMerchant'||dimensionCode=='dsPlatform') {//平台，商户号
            !isChangeNet && selectCorpsTable();
        }
    }
}

//平台变更回调
function platformChange(data) {
    // 平台变更时的处理逻辑
}

//商户号变更回调
function merchantChange(data) {
    // 商户号变更时的处理逻辑
}

//科目全选与自定义时显示
function subjectChange() {
    if($("input[name='subjectRadio']:checked").val()==0){
        $("#subjectDiv").hide();//科目多选下拉框
    }else{
        $("#subjectDiv").show();//科目多选下拉框
    }
}

//科目全选与自定义初始化显示
function subjectShow() {
    if($("#subjectRadioShow").val()==0){
        $("#subjectDiv").hide();//科目多选下拉框
    }else{
        $("#subjectDiv").show();//科目多选下拉框
    }
}

function addCondition(){
    $(".addClass2").show();
    $(".addClass3").hide();
    bt.dicts.init().get("endOperator").val(($("#endOperatorOld").val() == undefined || $("#endOperatorOld").val() == "" ||  $("#endOperatorOld").val() == "") ? "20" : $("#endOperatorOld").val());
    bt.dicts.init().get("numOperator").val(($("#numOperatorOld").val() == undefined || $("#numOperatorOld").val() == "" ||  $("#numOperatorOld").val() == "") ? "02" : $("#numOperatorOld").val());
    bt.moneyType.init().get("endNum").val($("#endNumOld").val());
    $("select[name='numOperator']").attr("lay-verify","required")
    $("select[name='endOperator']").attr("lay-verify","required")
}

function delCondition(){
    $(".addClass2").hide();
    $(".addClass3").show();
    $("select[name='endOperator']").attr("lay-verify","")
    $("select[name='numOperator']").attr("lay-verify","")
    bt.dicts.init().get("endOperator").val("");
    bt.dicts.init().get("numOperator").val("");
    bt.moneyType.init().get("endNum").val("");
}

function initConditionAssembly(){
    if($("#numOperatorOld").val() != null && $("#numOperatorOld").val() != ""  && $("#numOperatorOld").val() != undefined){
        addCondition()
    }else {
        delCondition()
    }
}

// 自定义字典展示 FUNDWARN_CORPACC
function initCorpAccSelectData(data){
    var dimensionCode =$("select[name='dimensionCode']").val();
    for (var i = 0; i < data.length; i++) {
        if(data[i].text == '全选' || data[i].text == '全部'){
            if(dimensionCode == 'corp'){
                data[i].text = '全部单位'
            }else if(dimensionCode == 'bankAcc'){
                data[i].text = '全部银行账号'
            }else if(dimensionCode == 'dsPlatform'){
                data[i].text = '全部单位'
            }else if(dimensionCode == 'dsMerchant'){
                data[i].text = '全部单位'
            }else {
                data[i].text = '全部内部账号'
            }
        }
    }
    return data
}

/**
 * 判断是否为空 true：空 false:不为空
 * @param data
 */
function isNull(data){
    return (data == "" ||  data == " " || data == undefined || data == null) ? true : false;
}



