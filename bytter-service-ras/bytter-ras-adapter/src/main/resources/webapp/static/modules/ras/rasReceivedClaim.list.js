var datatable;
var dicts;
var rasShops;
var corps;
var moneyType,dateFmt;
bt.use(['form','datatable','moneyType','corp','dicts','rasShop','dateType','date'], function(){
    $ = bt.jquery;
    bt.form();
    corps = bt.corp.init();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    rasShops=bt.rasShop.init();
    dateFmt=bt.date;
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[2,'asc'],[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        pageSizeOptions:[10,50,100,500,1000,2000,3000,5000],
        ajax: {
            'url': basePath+"/ras/received/claimDatatable"
        },
        buttons: [
            {
                //认领
                hide: bt.isnotResource('ras:receivedClaim:op'),
                text: bt.lang('ras.received.claim'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        claim(rowData);//批量提交
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            },{
                //自动匹配
                hide: bt.isnotResource('ras:receivedClaim:op'),
                text: bt.lang('ras.received.automatch'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    automatch(rowData);//批量提交
                }
            },{
                //剔除
                hide: bt.isnotResource('ras:receivedClaim:op'),
                text: bt.lang('ras.received.exclude'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        exclude(rowData);//批量提交
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            },{
                //取消认领
                hide: bt.isnotResource('ras:receivedClaim:op'),
                text: bt.lang('ras.received.cancelClaim'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        cancelClaim(rowData);//批量提交
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            }
        ]
        ,columns: [
            {data:"serialNo",className:'center',bSortable:true},
            {data:"orderNo",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                var name='';
                if(full.corpId!=null){
                    name = '('+full.corpCode+')'+full.corpName;
                }
                    return name;
                }
            },
            {data:"payeeAcc",className:'center',bSortable:true},
            {data:"payeeAccName",className:'center',bSortable:true},
            {data:"payeeBankName",className:'center',bSortable:true},
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"transDate",className:'center',bSortable:true},
            {data:"money",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.money);
                }
            },
            {data:"yhMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.yhMoney);
                }
            },{data:"transMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.transMoney);
                }
            },
            {data:"custName",className:'center',bSortable:true},
            {data:"custAcc",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true},
            {data:"dateSourceName",className:'center',bSortable:true},
            {data:"statusName",className:'center',bSortable:true},
            {data:"claimTypeName",className:'center',bSortable:true}
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").attr({"corpId":data.id});
}

//认领单位回调函数
function rasCorpCallback(data){
    bt.rasShop.get("rasShopId").attr({"corpId":data.id});
}

//认领
function claim(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var ids;
    var payType = dicts.get('rasPayType').val();
    var rasShopId = rasShops.get("rasShopId").val();
    var isClaim = false;
    var claimMsg = bt.lang('common.message.claim.confirmClaimUpdate');
    for(i=0;i<len;i++){
        if(bt.isnotResource('ras:receivedClaim:op',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }

        if(payType==" " && rasShopId==" "){
            layer.alert(bt.lang('common.message.claim.claimCheck'), {icon: 7});
            return;
        }

        if(rowData[i].claimType==1 && isClaim==false){
            claimMsg = bt.lang('common.message.config.claim.renewClaim');
            isClaim = true;
        }
        if(payType!=" " && rasShopId!=" " && isClaim==false){
            claimMsg = bt.lang('common.message.claim.confirmclaim');
        }
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
    }
    parent.layer.confirm(claimMsg,function(){
        bt.loading.show();
        $.post(basePath + "/ras/received/claim",{"rids":ids,"payType":payType,"rasShopId":rasShopId}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
            bt.loading.hide();
        },'JSON');
    });
}

//自动匹配
function automatch(data){
    var len = data.length;
    var claimMsg = bt.lang('common.message.claim.autoClaim');
    var claimType = dicts.get('claimType').val();
    if (claimType==0 && !len) {
        //待认领，没选择数据时提示是否根据查询条件过滤的数据做全部自动匹配!
            claimMsg = bt.lang('ras.message.claim.autoClaimAll');
            var corpId = corps.get("corpId").val();
            var payType = dicts.get('payType').val();
            if(payType==' '){
                payType="";
            }
            var shopId = rasShops.get("shopId").val();
            if(shopId==' '){
                shopId="";
            }
            var transDateVal = $("#transDate").val();
            var endTransDateVal = $("#led_transDate").val();
            var transMoney = moneyType.get("transMoney").val();
            var endTransMoney = moneyType.get("endMoney").val();
            var payeeAcc = $("#payeeAcc").val();
            parent.layer.confirm(claimMsg,function(){
                bt.loading.show();
                $.ajax({
                    type:"POST",
                    url: basePath+ "/ras/received/autoClaimAll",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify({'corpId':corpId,'shopId':shopId,'payType':payType,'transDateStart':transDateVal,
                        'transDateEnd':endTransDateVal,'transMoney':transMoney,'endTransMoney':endTransMoney,'payeeAcc':payeeAcc}),
                    success:function(result){
                        parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                        if(result.success){
                            bt.datatable.refreshCurrentPage();
                        }
                        bt.loading.hide();
                    }
                });
            });
    }else if((claimType==0 && len) || (claimType==1 && len)){
        if(!len){
            layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
            return;
        }
        var rowData = data.splice(0,len);
        var ids;
        var isClaim = false;
        for(i=0;i<len;i++){
            if(bt.isnotResource('ras:receivedClaim:op',rowData[i].corpId)){
                layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
                return;
            }
            if(rowData[i].claimType==1 && isClaim==false){
                claimMsg = bt.lang('common.message.config.claim.renewAutoClaim');
                isClaim = true;
            }
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        parent.layer.confirm(claimMsg,function(){
            bt.loading.show();
            $.post(basePath + "/ras/received/autoClaim",{"rids":ids}, function(result){
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    bt.datatable.refreshCurrentPage();
                }
                bt.loading.hide();
            },'JSON');
        });
    }
}

//剔除
function exclude(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var ids;
    for(i=0;i<len;i++){
        if(bt.isnotResource('ras:receivedClaim:op',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
        if(rowData[i].status!=0 && rowData[i].status!=1 && rowData[i].status!=-1){
            layer.alert(bt.lang('common.message.config.claim.statusCheck'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.exclude'),function(){
        $.post(basePath + "/ras/received/exclude",{"rids":ids}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

//取消认领
function cancelClaim(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var ids;
    for(i=0;i<len;i++){
        if(bt.isnotResource('ras:receivedClaim:op',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
        if(rowData[i].status!=0 && rowData[i].status!=1 && rowData[i].status!=-1){
            layer.alert(bt.lang('common.message.config.claim.statusCheck'), {icon: 7});
            return;
        }
        if(rowData[i].claimType!=1){
            layer.alert(bt.lang('common.message.config.claim.claimTypeCheck'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.cancelClaim'),function(){
        bt.loading.show();
        $.post(basePath + "/ras/received/cancelClaim",{"rids":ids}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
            bt.loading.hide();
        },'JSON');
    });
}

