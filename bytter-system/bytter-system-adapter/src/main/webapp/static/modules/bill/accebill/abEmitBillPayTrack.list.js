var billTable={};
var idsValArray=[];
bt.use(['form','datatable','corp','dicts','moneyType','btdate','element','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
        bt.corp.init();
        bt.dicts.init();
        bt.moneyType.init();
    bt.mulselect.init();
        bt.btdate.init({
            elem:"#startDate",
            startname:"ged_startDate",
            endname:"led_startDate"
        });
        bt.btdate.init({
            elem:"#endDate",
            startname:"ged_endDate",
            endname:"led_endDate"
        });
        //生成列表
        billTable= bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            colReorder: true,
            scrollY: '455px',
            checkbox:true,order: [[1,'desc']],
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath+"/bill/accebill/abemitbillpay/trackDatatable"
            },
            exportFile:{//输入文档设置
                printTitle:'',//设置打印标题 默认无标题
                outputTitle:function(){//设置导出标题 默认为父级当前选项卡内容，也可以传入方法
                    return bt.lang('common.button.cashing.online')
                },
                outputFilename:bt.lang('common.button.cashing.online')//设置导出文件名称  默认为父级选项卡内容或标题名称
            },
            buttons: [
                {
                    text: bt.lang('common.button.confrimepay'),
                    hide:bt.isnotResource('bill:accebill:abemitbillpay:confirm'),
                    action: function ( e, dt, node, config ) {
                        var data = dt.rows('.selected').data();
                        batchConfrimepay(data);
                    }
                }/*,
                {
                    text: bt.lang('common.button.exports'),
                    action: function ( e, dt, node, config ) {
                        exports();
                    }
                }*/
            ]
            ,columns: [
                {data:"billTypeName",bSortable:true},
                {data:"billFlagName",bSortable:true},
                {data:"insideSingName",bSortable:true},
                {data: "applyCorpName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                    }
                },
                {
                    data: "corpIdName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return '(' + full.corpCode + ')' + full.corpIdName;
                    }
                },
                {data: "busCorpName", bSortable: true},
                {data: "busPersonnel", bSortable: true},
                {data:"payer",bSortable:true},
                {data: "payeracc", bSortable: true},
                {data:"acceptor",bSortable:true},
                {data:"origNo",bSortable:true},
                {data:"billMoney",bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.money.format(full.billMoney);
                    }
                },
                {data:"startDate",bSortable:true},
                {data:"endDate",bSortable:true},
                {data:"payee",bSortable:true},
                {data:"statusName",bSortable:true},
                {data:"briefIdName",bSortable:true},
                {data:"billNo",bSortable:true},
                {data:"payStatusName",bSortable:true},
                {data:"voucherStatName",bSortable:true},
                {data:"newStmIssuofpprIndName",bSortable:true},
                {data:"batchNo",bSortable:true},
                {data:"blRmtrUnnIdNo",bSortable:true},
                {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    //查看、撤回、编辑、删除、确认已付款
                    //console.log(full.payStatus);
                    //console.log(full.voucherStat);
                    var html = "";
                    if(bt.isResource('bill:accebill:abemitbillpay:confirm',full.applyCorpId)){
                            html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                        if(full.payStatus==0){//付款单暂存状态
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="withdraw(this)">'+bt.lang("common.button.withdraw")+'</a>';
                        }else if(full.payStatus==-2||full.payStatus==-3){//付款单撤回或删除状态
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("common.button.edit")+'</a>\
                            <span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteTrack(this)">'+bt.lang("common.button.delete")+'</a>';
                        }else if(full.voucherStat==0){//交易成功状态
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="confrimepay(this)">'+bt.lang("common.button.confrimepay")+'</a>';
                        }
                    }
                    return html;
                }}
            ],
            /*fnDrawCallback:function(){
                idsValArray=new Array();
                setSumMoney();
            },*/
            btClickRowCallback:function(data){
                opIdsValArray(data);
            },
            btDrawCallback:function(setting){
                if(idsValArray.length>0) {
                    bt.datatable.onCheckRow("appTable", {
                        "key": "id",
                        "value": idsValArray,
                        "checked": true
                    });
                }
                setSumMoney();
            },
            fixedColumns:{
                rightColumns:1
            },
            summary:{
                pageSum:true,
                selectSum:true,
                sumList:[
                    {
                        colNum:'11',
                        dataName:'billMoney'
                    }
                ]
            }
        });

    if(parent.adjustMainWebHeight){
        setTimeout(function () {
            parent.adjustMainWebHeight($('body').height());
        },200)
    }
});
bt.use(['btdate'], function() {
        var date = new Date();
        var currentTime= date.Format("yyyy-MM-dd");
        var dd = bt.btdate.init({
            elem:".date-track",
            // labelname:bt.lang('table.abVoucherinfo.Business.date'),
            name:"date"});
        dd.setValue({value:currentTime})
});

/**
 * 批量确认已付款
 */
function batchConfrimepay(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    for(var i=0;i<len;i++){
        if (bt.isnotResource('bill:accebill:abemitbillpay:confirm',rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [ (i + 1) ]), {icon : 7});
            return;
        }
        if(rowData[i].voucherStat!=0){
            //所选票据存在非交易成功的票据，不可进行批量确认已付款操作，请重新选择。
            layer.alert(bt.lang('bill.accebill.billPay.track.confrimepay.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.confrimepay'),function(){
        var aids;
        for(var i=0;i<len;i++){
            if(aids){
                aids=aids+","+rowData[i].id;
            }else{
                aids=rowData[i].id;
            }
        }
        $.post(basePath + "/bill/accebill/abemitbillpay/batchConfrimepay/"+aids, function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
    			bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

/**
 * 撤回
 */
function withdraw(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.isnotResource('bill:accebill:abemitbillpay:confirm',data.applyCorpId)) {
        layer.alert(bt.lang('common.message.dataResourceCheck', [ (i + 1) ]), {icon : 7});
        return;
    }
    var aid = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm'),function(){
        $.post(basePath + "/bill/accebill/abemitbillpay/withdraw/" + aid, function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

/**
 * 修改
 */
function edit(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.isnotResource('bill:accebill:abemitbillpay:confirm',data.applyCorpId)) {
        layer.alert(bt.lang('common.message.dataResourceCheck', [ (i + 1) ]), {icon : 7});
        return;
    }
    var aid = data.id;
    /*if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/accebill/abemitbillpay/edit/"+aid);
        return;
    }*/
    if(parent.bt.navtab){
    	parent.bt.navtab.addNavtab(bt.lang('common.button.edit'),
    			basePath + "/bill/accebill/abemitbillpay/edit/"+aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '680px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbillpay/edit/"+aid,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

/**
 * 删除
 */
function deleteTrack(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.isnotResource('bill:accebill:abemitbillpay:confirm',data.applyCorpId)) {
        layer.alert(bt.lang('common.message.dataResourceCheck', [ (i + 1) ]), {icon : 7});
        return;
    }
    var aid = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/abemitbillpay/delete/" + aid, function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

/**
 * 付款已确认
 */
function confrimepay(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.isnotResource('bill:accebill:abemitbillpay:confirm',data.applyCorpId)) {
        layer.alert(bt.lang('common.message.dataResourceCheck', [ (i + 1) ]), {icon : 7});
        return;
    }
    var aid = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm.confrimepay'),function(){
        $.post(basePath + "/bill/accebill/abemitbillpay/confrimepay/" + aid, function(result){
        	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(parent.bt.navtab){
    	parent.bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abemitbillpay/view?viceId="+data.vvid+"&accId="+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbillpay/view?viceId="+data.vvid+"&accId="+data.id
    });
}

function exports(el){
    parent.layer.alert(bt.lang("common.message.indevelopment"));
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].id)<0){
                    idsValArray.push(datas[i].id);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].id);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);

}
