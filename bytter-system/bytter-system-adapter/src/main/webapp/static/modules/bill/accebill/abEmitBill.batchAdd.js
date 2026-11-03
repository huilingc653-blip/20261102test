var billTable,isBatchBill;
bt.use(['datatable','btUpload'], function(){
    var $ = bt.jquery;
    isBatchBill=$('#isBatchBill').val();
    if(isBatchBill=='1'){
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        paging:false,
        order: [[0,'desc']],
        searchForm: {
            formElem: "#searchForm",
        },
        ajax: {
            'url': basePath+"/bill/accebill/abemitbill/datatable"
        },columns: [
            {data:"appNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"insideSingName",bSortable:true},
            {data:"emitTypeName",bSortable:true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"emitGuandisSignName",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"payeeacc",bSortable:true},
            {data:"payeeBankName",bSortable:true},
            {data:"sourceName",bSortable:true},
            {data:"sendSignName",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"isEnsureName",bSortable:true},
            {data:"attachmentNum",bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="##" data-row="'+meta.row+'" onclick="attachmentView(this)">'+bt.lang("bill.accebill.attachment.num",[(full.attachmentNum||0)])+'</a>';
                    return html;
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="##" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    html = html+'<span class="text-explode">|</span><a class="" href="##" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                    return html;

            }}
        ],
        fixedColumns:{
            rightColumns:2
        },
        btClickRowCallback:function(data){
            if(data&&data.length&&data.length>0){
                location.href = basePath + "/bill/accebill/abemitbill/update/"+data[0].aid+"?isBatchBill=1&batchNo="+$("#batchNo").val();
            }
        },
        summary:{
            pageSum:true,
            sumList:[
                {
                    colNum:'9',
                    dataName:'billMoney'
                }
            ]
        }
    });
    }

});

var batchEmitSubmit=function(isSubmit){
    if(isSubmit){
        var emitDataParams=getEmitDataParams();
        if(!emitDataParams){
            bt.loading.hide();
            return;
        }
        if(emitDataParams.vids==""){
            layer.alert(bt.lang('bill.accebill.emit.batchSave'), {icon: 7});
            bt.loading.hide();
            return;
        }
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
            bt.loading.show();
            $.post(basePath + "/bill/accebill/abemitbill/batchSubmit",emitDataParams, function(result){
                bt.loading.hide();
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    bt.navtab.closeCurrent();
                }
            },'JSON');
        });
    }else{
        initAttachmentInfo();
        var emitInfo={
            billMoney:$("#billMoney").val(),
            payer:$("#payer").val(),
            payee:$("#payeeAccName").val(),
            acceptor:$("#acceptor").val(),
            endDate:dateCheck.get("endDate").val()
        };
        if(batchEmitRepeatingCheck(emitInfo)){
            //证书签名
            var signDataMsg = signData("开票登记新增", 9);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
            var subData = $('#inputForm').serialize();
            if($('#inputForm').serialize().indexOf("billFlag=") < 0){
                subData = $('#inputForm').serialize()+"&billFlag="+billFlagVal;
            }
            $.post(basePath + '/bill/accebill/abemitbill/save',subData,function(result){
                bt.loading.hide();
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    // emitData.vids=emitData.vids+","+result.attributes.vids;
                    // emitData.vvids=emitData.vvids+","+result.attributes.vvids;
                    // emitData.aids=emitData.aids+","+result.attributes.aids;
                    $("#appNo").val(result.attributes.appNo);
                    $("#appNoLabel").text(result.attributes.appNo);
                    resetAttachmentInfo();
                    bt.datatable.refreshCurrentPage();
                }
            },'JSON');
        }else {
            bt.loading.hide();
        }
    }
}

function getEmitDataParams(){
    var data=billTable.data();
    var emitData={"vids":"","vvids":"","aids":""};
    if(data && data.length>0){
        var sumBillMoney=0;
        for(var i=0;i<data.length;i++){
            sumBillMoney=bt.money.add(sumBillMoney,bt.money.toNumber(data[i].billMoney));
            if(i==0){
                emitData.vids=data[i].vid;
                emitData.vvids=data[i].vvid;
                emitData.aids=data[i].aid;
            }else{
                emitData.vids += ","+data[i].vid;
                emitData.vvids += ","+data[i].vvid;
                emitData.aids += ","+data[i].aid;
            }
        }

        var emitType=dicts.get("emitType").val();
        if($("#isBillPool").val()
            && emitType=='10'
            && sumBillMoney>bt.money.toNumber($('#poolLimitValidMoneyVal').val())){
            layer.alert(bt.lang('bill.accebill.batchEmit.sumMoneyGrePoolMeony'), {icon: 7});
            return false;
        }
    }
    return emitData;
}

function batchEmitRepeatingCheck(emitInfo){
    var data=billTable.data();
    var sumBillMoney=bt.money.toNumber(emitInfo.billMoney);
    if(data && data.length>0){
        for(var i=0;i<data.length;i++){
            if(data[i].payer!=emitInfo.payer
                ||(data[i].acceptor||"")!=emitInfo.acceptor){
                layer.alert(bt.lang('bill.accebill.batchEmit.eqInfo'), {icon: 7});
                return false;
            }
            sumBillMoney=bt.money.add(sumBillMoney,bt.money.toNumber(data[i].billMoney));
            //不需要处理重复金额开票场景
            // else if(data[i].payee==emitInfo.payee
            //     &&Number(data[i].billMoney)==Number(emitInfo.billMoney)
            //     &&data[i].endDate==emitInfo.endDate
            //     ){
            //     layer.alert(bt.lang('bill.accebill.batchEmit.noeqInfo'), {icon: 7});
            //     return false;
            // }
        }
    }
    var emitType=dicts.get("emitType").val();
    if($("#isBillPool").val()
        && $('#poolLimitValidMoneyVal').val()!="false"
        && emitType=='10'
        && sumBillMoney>bt.money.toNumber($('#poolLimitValidMoneyVal').val())){
        layer.alert(bt.lang('bill.accebill.batchEmit.sumMoneyGrePoolMeony'), {icon: 7});
        return false;
    }
    return true;
}

function deleteColumn(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    // parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/abemitbill/delete",{"aid":aid}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                // emitData.vids=emitData.vids.replace(","+data.vid,"");
                // emitData.vvids=emitData.vvids.replace(","+data.vvid,"");
                // emitData.aids=emitData.aids.replace(","+data.aid,"");
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    // });
    var e = window.event || arguments.callee.caller.arguments[0];
    e.stopPropagation();
}

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;

    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
            basePath + "/bill/accebill/abemitbill/view/" + aid);
    }else{
        parent.layer.open({
            type: 2,
            area: ['1080px', '720px'],
            maxmin: true,
            content: basePath + "/bill/accebill/abemitbill/view/" + aid
        });
    }
    var e = window.event || arguments.callee.caller.arguments[0];
    e.stopPropagation();
}

function attachmentView(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var isNotOp=data.status!=11?'0':'1';
    var params={
        "id":data.aid,
        "attachmentId":(data.attachmentId||null),
        "vvid":(data.vvid||null),
        "briefId":(data.briefId||null),
        "isNotOp":isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end:function(){
            billTable.draw();
        }
    });
    var e = window.event || arguments.callee.caller.arguments[0];
    e.stopPropagation();
}
//附件初始化
function initAttachmentInfo(){
    var attachmentData=uploader.getData();
    if(attachmentData){
        $("#attachmentId").val(uploader.getGroupId());
        $("#attachmentNum").val(attachmentData.length);
    }
}

//重置附件控件
function resetAttachmentInfo(){
    $("#uploader").html("");
    uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    $("#attachmentId").val("");
    $("#attachmentNum").val("");
}