var moneyType,moneyFun,dicts,form,dateFun;

//读取并解析对象
var temp = localStorage.getItem($('#docId').val());
var dataVo = JSON.parse(temp);
$("#relationId").val(dataVo.relationId);
$("#writeOffType").val(dataVo.writeOffType);
$("#paymentMeans").val(dataVo.paymentMeans);
$("#exchangeRate").val(dataVo.exchangeRate);


bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
     var $ = bt.jquery; form = bt.form();
     moneyType = bt.moneyType.init();
     moneyFun = moneyType.getFun();
     dicts = bt.dicts.init();
     dateFun = bt.dateType.getFun();


});

//跳转核销确认页面检查
function sureWriteOffCheck(){
    if(checkDtlRule()==true){
        //删除缓存数据
        localStorage.removeItem($('#docId').val());
        //关闭当前弹窗
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
        //跳转核销确认页面
        sureWriteOff();
    }else if(checkDtlRule()==false) {
//        parent.layer.alert(bt.lang('不满足自动匹配条件'), {icon: 2});

        //跳转不满足自动匹配条件弹框页面
        parent.layer.customConfirm('不满足自动匹配条件', {icon: 2}, ['取消','返回设置'], function(index){
            parent.layer.closeAll();
//        parent.layer.close(index);
//        var index1 = parent.layer.getFrameIndex(window.name);
//        parent.layer.close(index1);
        }, function(index){
            parent.layer.close(index);
        }, 'listDocManageCustomStyle');
    }
    return false
}



//记录满足勾选的所有自动核销匹配规则的应收单和流水组合
var docAndDtlList=[];

//记录满足勾选的所有自动核销匹配规则的应收单id组合字符串
var docId = '';

//记录满足勾选的所有自动核销匹配规则的流水id组合字符串
var dtlId = '';


//判断是否符合匹配规则
function checkDtlRule(){
    var docManageApproveVoList = dataVo.docManageApproveVoList;
    var docManageApproveVoL = JSON.parse(docManageApproveVoList);
    var bisAccDtlVOList = dataVo.bisAccDtlVOList;
    var bisAccDtlVOL = JSON.parse(bisAccDtlVOList);
    var bankAccTypeStr = $("#bankAccTypeStr").val();
    var checkDtlRuleFlag = true;
    if(!bankAccTypeStr){
        //未设置 自动核销匹配规则
        return;
    }
    if(bankAccTypeStr.indexOf("4") != -1){  //自动匹配规则：到期日期
        var ttr = dicts.get("transTimeRule").val();
        if(ttr.trim().length==0){
            //未设置 到期日期匹配规则
            return;
        }
    }

    //遍历勾选的应收单数据
    for(var i=0;i<docManageApproveVoL.length;i++){
        //遍历勾选的流水数据
        for(var j=0;j<bisAccDtlVOL.length;j++){
            var checkDtlRuleFlag1 = true;

            if(checkDtlRuleFlag1 && bankAccTypeStr.indexOf("1") != -1){  //自动匹配规则：应收余额
                checkDtlRuleFlag1 = Number(docManageApproveVoL[i].writeOffBal)-Number(bisAccDtlVOL[j].writeOffBal) == 0;
            }
            if(checkDtlRuleFlag1 && bankAccTypeStr.indexOf("2") != -1){  //自动匹配规则：付款账号
                checkDtlRuleFlag1 = docManageApproveVoL[i].payerAccNo == bisAccDtlVOL[j].oppAccNo;
            }
            if(checkDtlRuleFlag1 && bankAccTypeStr.indexOf("3") != -1){  //自动匹配规则：收款账号
                checkDtlRuleFlag1 = docManageApproveVoL[i].payeeAcc == bisAccDtlVOL[j].bankAcc;
            }
            if(checkDtlRuleFlag1 && bankAccTypeStr.indexOf("4") != -1){  //自动匹配规则：到期日期
                let transTimeRule = dicts.get("transTimeRule").val();
                var endDate = docManageApproveVoL[i].endDate;
                var transTime = bisAccDtlVOL[j].transTime.substr(0,10);
                if(transTimeRule == "0"){
                    //相等
                    checkDtlRuleFlag1 = dateFun.getBetweenDay(endDate, transTime) == 0;
                }else if(transTimeRule == "-1"){
                    //流水日期小于到期日期
                    checkDtlRuleFlag1 = dateFun.getBetweenDay(endDate, transTime) < 0;
                }else if(transTimeRule == "1"){
                    //流水日期大于到期日期
                    checkDtlRuleFlag1 = dateFun.getBetweenDay(endDate, transTime) > 0;
                }else{
                    //未设置 到期日期匹配规则
                    return;
                }
            }
            if(checkDtlRuleFlag1 && bankAccTypeStr.indexOf("5") != -1){  //自动匹配规则：应收币种
                checkDtlRuleFlag1 = docManageApproveVoL[i].curId == bisAccDtlVOL[j].curId;
            }

            //当前应收单和流水满足勾选的所有自动核销匹配规则
            if(checkDtlRuleFlag1){
                docAndDtlList.push({
                    docVo: docManageApproveVoL[i],
                    dtlVo: bisAccDtlVOL[j]
                });
            }

        }
    }

    //不满足一对一匹配要求的应收单id
    var docVoIdArr = [];
    //不满足一对一匹配要求的流水id
    var dtlVoIdArr = [];

    //遍历docAndDtlList
    for(var t=0;t<docAndDtlList.length;t++){
        var docVoId1 = docAndDtlList[t].docVo.id;
        var dtlVoId1 = docAndDtlList[t].dtlVo.id;
        //再次遍历docAndDtlList
        for(var v=0;v<docAndDtlList.length;v++){
            if(v==t){
                //不与自己本身这组比较
                continue;
            }
            var docVoId2 = docAndDtlList[v].docVo.id;
            var dtlVoId2 = docAndDtlList[v].dtlVo.id;
            if(docVoId1==docVoId2){
                //保存起来后面用做删除
                docVoIdArr.push(docVoId1);
            }
            if(dtlVoId1==dtlVoId2){
                //保存起来后面用做删除
                dtlVoIdArr.push(dtlVoId1);
            }
        }
    }

    //不满足一对一匹配要求的应收单id
    for(var d=0;d<docVoIdArr.length;d++){
        //遍历docAndDtlList，删除不满足一对一匹配要求的应收单id和流水id的组合
        for(var del=docAndDtlList.length-1;del>=0;del--){ //倒序循环，避免索引问题
            if(docAndDtlList[del].docVo.id==docVoIdArr[d]){
                docAndDtlList.splice(del,1) //删除
            }
        }
    }

    //不满足一对一匹配要求的流水id
    for(var d=0;d<dtlVoIdArr.length;d++){
        //遍历docAndDtlList，删除不满足一对一匹配要求的应收单id和流水id的组合
        for(var del=docAndDtlList.length-1;del>=0;del--){ //倒序循环，避免索引问题
            if(docAndDtlList[del].dtlVo.id==dtlVoIdArr[d]){
                docAndDtlList.splice(del,1) //删除
            }
        }
    }

    if(docAndDtlList.length==0){
        checkDtlRuleFlag = false;
    }else{
        //将满足一对一匹配要求的应收单id和流水id各自组合起来
        for(var z=0;z<docAndDtlList.length;z++){
            docId += docAndDtlList[z].docVo.id+',';
            dtlId += docAndDtlList[z].dtlVo.id+',';
        }
    }

    return checkDtlRuleFlag;
}


//跳转核销确认页面
function sureWriteOff(){

    var writeOffType = $("#writeOffType").val();
    var paymentMeans = $("#paymentMeans").val();
    var bankAccTypeStr = $("#bankAccTypeStr").val();
    var exchangeRate = $("#exchangeRate").val();

    //用'+'将规则对应语句进行拼接
    var bankAccTypeS = '';
    if(bankAccTypeStr.indexOf("1") != -1){  //自动匹配规则：应收余额
        bankAccTypeS += '金额相等+';
    }
    if(bankAccTypeStr.indexOf("2") != -1){  //自动匹配规则：付款账号
        bankAccTypeS += '付款账号等于对方账号+';
    }
    if(bankAccTypeStr.indexOf("3") != -1){  //自动匹配规则：收款账号
        bankAccTypeS += '收款账号等于本方账号+';
    }
    if(bankAccTypeStr.indexOf("4") != -1){  //自动匹配规则：到期日期
        let transTimeRule = dicts.get("transTimeRule").val();
        if(transTimeRule == "0"){
            bankAccTypeS += '到期日期相等+';
        }else if(transTimeRule == "-1"){
            bankAccTypeS += '流水日期小于到期日期+';
        }else if(transTimeRule == "1"){
            bankAccTypeS += '流水日期大于到期日期+';
        }
    }
    if(bankAccTypeStr.indexOf("5") != -1){  //自动匹配规则：应收币种
        bankAccTypeS += '币种一致+';
    }
    bankAccTypeS = bankAccTypeS.slice(0,-1);  //删除最后一个字符'+'

    var data = {};
    data.docId = docId;
    data.bankAccTypeStr = bankAccTypeS;

    //缓存数据
    localStorage.setItem(docId, JSON.stringify(data));


    bt.loading.hide();
    parent.layer.open({
        type: 2,
        area: ['1200px', '650px'],
        maxmin: true,
        title: '自动核销确认页',
        content: basePath + "/accountReceivable/writeOff/viewSureAuto?docId="+docId+"&relationId="+dtlId+
                            "&writeOffType="+writeOffType+"&paymentMeans="+paymentMeans+
                            "&exchangeRate="+exchangeRate,
        end:function(){
        }
    });
}

function matchingRuleCallback(data) {
    var bankAccTypeStr = '';
    var flag = false;
    for(var i=0;i<data.length;i++){
        bankAccTypeStr += data[i].value + ",";
        if (data[i].value === '4') {
            flag = true;
        }
    }
    $("#bankAccTypeStr").val(bankAccTypeStr);
    if(flag){
        $(".transTimeRuleTr").show();
        dicts.get("transTimeRule").attr({"data-required":true});
    }else{
        $(".transTimeRuleTr").hide();
        dicts.get("transTimeRule").attr({"data-required":false});
    }
}


function closeOpen(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
}