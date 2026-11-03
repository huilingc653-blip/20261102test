var dicts,dateType,warnDataTable,selectCorpsTable,selectBankAccInfoTable,selectAccInfoTable,moneyType,bankAccAtt,bankAccNature,isAttribute;
bt.use(['form','element','datatable','select','checkboxs','selectType','moneyType','corp','dicts','dateType','mulCorp','mulBankAccInfo','mulAccInfo'], function() {
    var $ = bt.jquery, form = bt.form();
    var element = layui.element;
    var corps = bt.corp.init();
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    dateType = bt.dateType.init();
    var select = bt.selectType.init();
    var mulCorp=bt.mulCorp.init();
    var mulBankAccInfo=bt.mulBankAccInfo.init();
    var mulAccInfo=bt.mulAccInfo.init();
    // bt.mulselect.init();
    initCorpNames();
    loadSubjectName();

    function initCorpNames(){
        var corpNames = $("#corpNamesBox").data('value');
        if(corpNames){
            corpNames = corpNames.split(',');
            var html = ''
            bt.each(corpNames,function (i,item){
                html += '<div class="layui-col-xs4">'+item+'</div>'
            })
            $("#corpNamesBox").html(html);
        }
    }

    function loadSubjectName(){
        $.get(basePath + "/finacial/paramconfig/fcsubject/jsonSubjectDate",{},function(data){
            if(data){
                var selectedValue = $("#subjectNameBox").data('value');
                var html = '';

                bt.each(data,function (i,item){
                    if(selectedValue.indexOf(item.id) !== -1){
                        if(html == ''){
                            html = item.subjectName;
                        }else {
                            html = html + ',' + item.subjectName;
                        }
                        //html += '<div class="layui-col-xs6 layui-col-sm4 layui-col-md3 layui-col-lg2" id="'+item.id+'">'+item.subjectName+'</div>'
                    }
                })
                if(html != ''){
                    html = '<div style="border: 1px solid black; width: 1000px;white-space: normal;word-break: break-word">' + html + '</div>';
                }

                $("#subjectNameBox").html(html);
            }
        },"json")
    }

    function gtCheckBoxHtml(data,name,checkValues,el){
        data.sort(function(a,b){
            return a.index - b.index;
        });

        if(data){
            var temp = '';
            bt.each(data,function (i,item){
                temp += '<input type="checkbox"  lay-skin="primary" name="'+ name+'"' +' '
                temp += 'title="'+item.text+'"'+' '
                temp += 'value="'+item.value+'"'+' '
                temp += 'disabled=""'+' '
                if("all" == checkValues ||(checkValues && checkValues.indexOf(item.value) !== -1)){
                    temp += "checked";
                }

                temp += '>'
            })
            $(el).html(temp)
        }
    }


    function createCheckboxDict(dictType,name,el,checkValues,url,textField){
        var data = [];
        if(dictType != null && dictType != ""){
            var dicts = bt.dictContext[dictType];
            if(!dicts){
                throwError("字典组:"+dictType+"不存在")
            }
            for(var value in dicts){
                var key = dicts[value];
                data.push({
                    value:value,
                    text:key.name,
                    index:key.index
                });
            }
            gtCheckBoxHtml(data,name,checkValues,el);
        }else if(url){
            $.ajax({
                type: 'get',
                url: basePath + url,
                dataType: 'json',
                success: function(result, status, xhr) {
                   if(!result){
                       throwError("获取数据失败")
                   }
                    for(var index = 0; index < result.length; index++){
                        var dt = result[index];
                        data.push({
                            value:dt.id,
                            text:dt[textField],
                            index:index
                        });
                    }
                    gtCheckBoxHtml(data,name,checkValues,el);
                    form.render('checkbox');
                },
            });
        }

    }

    createCheckboxDict('bankacconline','bankAccOnline','#bankAccOnline',$("#bankAccOnlineValue").val())
    createCheckboxDict('btBankAcc.accType','bankAccType','#bankAccType',$("#bankAccTypeValue").val())
    createCheckboxDict(null,'bankAccAttribute','#bankAccAtt',$("#bankAttStr").val(),"/bankacc/bisaccattribute/json","attributeName")
    createCheckboxDict(null,'bankAccNature','#bankAccNature',$("#bankAccNatureStr").val(),"/bankacc/btaccnature/json","natureName")
    form.render('checkbox');

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
        var corpIds;
        $.each(data,function (key, val) {
                if(key==0){
                    corpIds=val.id;
                }else if(key>0){
                    corpIds=corpIds+","+val.id;
                }

            }
        )
        $("#corpIdAndAccStr").val(corpIds);
        showAccTable();//刷新表格
    }
    //单位选择调用控件前传递netId
    bt.externalNetIdParams=function(){
        if($("#netIdStr").val()==""){
            layer.alert(bt.lang('请先选择网点'), {icon: 7});
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
        showAccTable();//刷新表格
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
        showAccTable();//刷新表格
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
    /*bankAccAtt=bt.checkboxs.init({
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
    });*/
    //内部账号性质复选框
   /* bankAccNature=bt.checkboxs.init({
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
    });*/

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
        if($("#corpIdAndAccStr").val()=="" || $("#corpIdAndAccStr").val()=="select" ){
            //预警维度
            if($("select[name='dimensionCode']").val()=='corp'){
                layer.alert(bt.lang('table.btwarnstrategy.corpselect.errorMessage'), {icon: 7});
            }else{
                layer.alert(bt.lang('table.btwarnstrategy.accountselect.errorMessage'), {icon: 7});
            }
            return false;
        }
        //业务属性选择判断
        var bankAttStr="";
        var bankAccTypeStr="";
        var bankAccOnlineStr="";
        var bankAccNatureStr="";
        var subjectNameStr="";
        if(isAttribute=="bank" ){
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
                if($(this).val()=="all"){
                    bankAttStr="all";
                }else{
                    if(bankAttStr!="all"){
                        bankAttStr += $(this).val() + ",";
                    }
                }
            });
            $('input[name="bankAccType"]:checked').each(function () {
                if($(this).val()=="all"){
                    bankAccTypeStr="all";
                }else{
                    if(bankAccTypeStr!="all"){
                        bankAccTypeStr += $(this).val() + ",";
                    }
                }
            });
            $('input[name="bankAccOnline"]:checked').each(function () {
                if($(this).val()=="all"){
                    bankAccOnlineStr="all";
                }else{
                    if(bankAccOnlineStr!="all"){
                        bankAccOnlineStr += $(this).val() + ",";
                    }
                }
            });
        }else if(isAttribute=="interior"){
            if(dataInfo.bankAccNature==""){
                layer.alert(bt.lang('table.btwarnstrategy.bankAccNature.errorMessage'), {icon: 7});
                return false;
            }
            $('input[name="bankAccNature"]:checked').each(function () {
                if($(this).val()=="all"){
                    bankAccNatureStr="all";
                }else{
                    if(bankAccNatureStr!="all"){
                        bankAccNatureStr += $(this).val() + ",";
                    }
                }
            });

            if($("input[name='subjectRadio']:checked").val()=='0'){
                subjectNameStr="all";
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
function changeView() {
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
    $("#accSelectH2").html(bt.lang('table.btwarnstrategy.accountselect.title'));//账号选择模块标题
    $("#bankAccDiv").hide();//单位银行账号业务属性Div
    $("#interiorAccDiv").hide();//单位内部账号业务属性Div
    isAttribute="no";
    if( typeCode=='balOver' ) {//余额超标
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.sumbalance'));//条件模块条件头
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.bankacc.balance'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='eqCorpPay'){//相同公司付款
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.dealmoney'));
            corpChangeShow();
        }
    }else if(typeCode=='bankAccPay'){//大额账户支出
        if(dimensionCode=='bankAcc'){//银行账号
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.dealmoney'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='daySumPay'){//单日累计支付
        if(dimensionCode=='corp'){//单位
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
        }
    }else if(typeCode=='oldBankAcc'){//久悬户预警
        if(dimensionCode=='bankAcc'){//银行账号
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.oldBankAcc.head'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='inflowOver'){//净流入透支
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.fdigdp'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.bankacc.fdigdp'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='balDown'){//账户余额持续下降
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpbankacc.endmonth.balancedecline'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.bankacc.endmonth.balancedecline'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='expendInflow'){//支出户流入
        if(dimensionCode=='corp'){//单位
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpflowintomoneycollect'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.flowintomoney'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }else if(typeCode=='cashOver'){//库存现金超标
        if(dimensionCode=='corp'){//单位
            $("#accSelectH2").html(bt.lang('table.btwarnstrategy.corpselect.title'));
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.corpinterioracc.balancecollect'));
            $("#mulCorpsDiv").show();//单位选择
            $("#operationattributeDiv").show();//业务属性选择模块
            $("#interiorAccDiv").show();//单位内部账号业务属性Div
            isAttribute="interior";
        }else if(dimensionCode=='fcAcc'){//内部账户
            $("#mulAccDiv").show();//内部账户选择
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.interioracc.balance'));
        }
    }else if(typeCode=='depositLoanOver'){//存贷比超标
        if(dimensionCode=='corp'){//单位
            $("#accSelectH2").html(bt.lang('table.btwarnstrategy.corpselect.title'));
            $("#remarkTr").show();
            $("#mulCorpsDiv").show();//单位选择
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.loantodeposit.exceedwarning')+'%');
            $("#remarkTd").html(bt.lang('btwarnstrategy.condition.warn1')+'\n\r<br/>' +
                '                '+bt.lang('btwarnstrategy.condition.warn2'));
        }
    }else if(typeCode=='cashPay'){//大额现金支出
        if(dimensionCode=='fcAcc'){//内部账户
            $("#mulAccDiv").show();//内部账户选择
            $("#warnConditionHead").html(bt.lang('condition.btwarnstrategy.interioracc.dealbalance'));
        }
    }else if(typeCode=='outPay'){//系统外付款
        $("#conditionTr").hide();
        if(dimensionCode=='corp'){//单位
            $("#remarkTr").show();
            $("#remarkTd").html(bt.lang('condition.btwarnstrategy.corpbankacc.sysouterpayment.message'));
            corpChangeShow();
        }else if(dimensionCode=='bankAcc'){//银行账号
            $("#remarkTr").show();
            $("#remarkTd").html(bt.lang('condition.btwarnstrategy.bankacc.sysouterpayment.message'));
            $("#mulBankAccDiv").show();//银行账户选择
        }
    }
    if(typeCode=='oldBankAcc'){//久悬户预警
        $("#forwardTimeId").show();
        bt.moneyType.init().get("forwardTime").attr("lay-verify","required|strlength")
        bt.moneyType.init().get("forwardTime").val($("#forwardTimeOld").val())
    } else {
        $("#forwardTimeId").hide();
        bt.moneyType.init().get("forwardTime").attr("lay-verify","strlength")
        bt.moneyType.init().get("forwardTime").val("")
    }
    showAccTable();
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
    var ids = "";
    for(var i=0;i<obj.length;i++){
        if(ids.length == 0){
            ids = obj[i].id;
        }else {
            ids = ids + "," + obj[i].id;
        }
    }
    $("#netIdStr").val(ids);
    corpAccSelectCallback();
}

function corpAccSelectCallback() {
    if($("#netIdStr").val()==""){
        layer.alert(bt.lang('请先选择网点'), {icon: 7});
    }else {
        if($("input[name='corpAccSelect']:checked").val()==0){
            $("#corpIdAndAccStr").val("all");
            $("#mulCorpsDiv").hide();//单位选择
            $("#mulBankAccDiv").hide();//银行账户选择
            $("#mulAccDiv").hide();//内部账户选择
            showAccTable();
        }else{
            $("#corpIdAndAccStr").val("select");
            $("#mulCorpsDiv").hide();//单位选择
            changeView();
        }
    }
}

//单位、银行账户、内部账户统计主体筛选列表显示
function showAccTable(){
    //预警类型
    var typeCode =$("select[name='typeCode']").val();
    //预警维度
    var dimensionCode =$("select[name='dimensionCode']").val();
    if(typeCode && dimensionCode
        && $.trim(typeCode) && $.trim(dimensionCode)){
        if(dimensionCode=='corp'){//单位
            selectCorpsTable();
        }else if(dimensionCode=='bankAcc'){//银行账号
            selectBankAccInfoTable();
        }else if(dimensionCode=='fcAcc') {//内部账户
            selectAccInfoTable();
        }
    }
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
            if($("select[name='dimensionCode']").val() == 'corp'){
                data[i].text = '全部单位'
            }else if($("select[name='dimensionCode']").val() == 'bankAcc'){
                data[i].text = '全部银行账号'
            }else {
                data[i].text = '全部内部账号'
            }

        }
    }
    return data
}



