var billTable={};
bt.use(['form','datatable','btdate','corp','select','dateType','dicts','bank'], function(){
    $ = bt.jquery,form = bt.form();
    corpCallback = function(ids){
        corpCallbackFun(ids);
    }
    bt.corp.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank'
    });

    //加载凭证种类下拉列表
    bt.select.init({
        elem:'.selectVouTid',
        name:"eqs_vouTid",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValidByInbox"
    });
    init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/blankvou/bvvouinfodtl/datatable"
        },
        buttons: [
            /*{
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text:bt.lang('common.button.edit'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    layer.msg(JSON.stringify(rowData));
                }
            },*/{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:null,name:"vouNo",bSortable:true,render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" style="color:blue" data-row="'+meta.row+'" onclick="view(this)">'+data.vouNo+'</a>';
                    return html;
                }},
            {data:"voucherTypeName",bSortable:true},
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"bankName",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"storageBoxName",bSortable:true},
            {data:"inputDate",bSortable:true},
            {data:"createName",bSortable:true},
            {data:"vouStatusName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = "";
                    //01已使用、02已出售、03已使用、04已退票、05已丢失、06已作废
                    if(data.vouStatus=="01"){
                        //已入库可做业务：使用、出售、作废、丢失、移库(由于要选择移库对象，只提供批量移库)
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:use')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html+='<a class="" href="#" data-row="'+meta.row+'" onclick="use(this)">'+bt.lang("common.button.use")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:sell')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html+='<a class="" href="#" data-row="'+meta.row+'" onclick="sell(this)">'+bt.lang("common.button.sell")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:lose')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html+='<a class="" href="#" data-row="'+meta.row+'" onclick="lose(this)">'+bt.lang("common.button.lose")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:cancel')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html+='<a class="" href="#" data-row="'+meta.row+'" onclick="tovoid(this)">'+bt.lang("common.button.tovoid")+'</a>';
                        }
                    }/*else if(data.vouStatus=="02"){
            		//已出售可做业务:无
            	}*/else if(data.vouStatus=="03"){
                        //已使用可做业务：退票、丢失
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:returnTicket')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html+='<a class="" href="#" data-row="'+meta.row+'" onclick="retreat(this)">'+bt.lang("common.button.retreat")+'</a>';
                        }
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:lose')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html+='<a class="" href="#" data-row="'+meta.row+'" onclick="lose(this)">'+bt.lang("common.button.lose")+'</a>';
                        }
                    }else if(data.vouStatus=="04"){
                        //已退票可做业务:作废
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:cancel')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html += '<a class="" href="#" data-row="'+meta.row+'" onclick="tovoid(this)">'+bt.lang("common.button.tovoid")+'</a>';
                        }
                    }else if(data.vouStatus=="05"){
                        //已丢失可做业务:作废
                        if(bt.isResource('bill:blankvou:bvVouInfoDtl:cancel')){
                            if(html){
                                html+='<span class="text-explode">|</span>';
                            }
                            html += '<a class="" href="#" data-row="'+meta.row+'" onclick="tovoid(this)">'+bt.lang("common.button.tovoid")+'</a>';
                        }

                    }/*else{
            		//06已作废可做业务:无
            	}*/
                    return html;
                }}
        ],
        fixedColumns:{
            rightColumns:1
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //批量出售
    form.on('submit(batchSell)', function(data){
        batchOperate("batchSell");
        return false;
    });
    //批量丢失
    form.on('submit(batchLose)', function(data){
        batchOperate("batchLose");
        return false;
    });
    //批量作废
    form.on('submit(batchTovoid)', function(data){
        batchOperate("batchTovoid");
        return false;
    });
    //批量退票
    form.on('submit(batchRetreat)', function(data){
        batchOperate("batchRetreat");
        return false;
    });
    //批量移库
    form.on('submit(batchTransf)', function(data){
        batchOperate("batchTransf");
        return false;
    });
});
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinfodtl/view/" + data.id
    });
}
function use(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var flag = true;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouemployinfo/add/" + data.id,
        cancel: function(index, layero){
            flag = false;
        },
        end:function(){
            if(flag){
//        		billTable.draw();
                bt.datatable.refreshCurrentPage();
            }
        }
    });
}
function sell(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.sell'),function(){
        $.post(basePath + "/bill/blankvou/bvvouinfodtl/sell/" + data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }else{
                parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
function retreat(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '249px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinfodtl/retreatPage/" + data.id,
        end:function(){
//            billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}
function lose(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.lose'),function(){
        $.post(basePath + "/bill/blankvou/bvvouinfodtl/lose/" + data.id, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }else{
                parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
function tovoid(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '249px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinfodtl/tovoidPage/" + data.id,
        end:function(){
//            billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

//批量操作
function batchOperate(operate){
    var data = billTable.rows('.selected').data();
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var lang = "",subPath="";
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
        if(bt.isnotResource('bill:accebill:abemitbill:delete',rowData[i].corpId)){
            layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
            return;
        }
    }
    //批量出售-已入库状态才能操作
    if(operate=="batchSell"){
        for(i=0;i<len;i++){
            if(rowData[i].vouStatus!=01){//01 已入库
                layer.alert(bt.lang('bill.blankvou.bvvouInfoDtl.batchOperate01'), {icon: 7});
                return;
            }
        }
        lang = 'common.message.confirm.batchSell';
        subPath = "/bill/blankvou/bvvouinfodtl/batchSell";
        handle1(lang,len,rowData,subPath);
        //批量丢失-已入库、已使用状态才能操作
    }else if(operate=="batchLose"){
        for(i=0;i<len;i++){
            if(rowData[i].vouStatus!=01&&rowData[i].vouStatus!=03){//01 已入库 03已使用
                layer.alert(bt.lang('bill.blankvou.bvvouInfoDtl.batchOperate02'), {icon: 7});
                return;
            }
        }
        lang = 'common.message.confirm.batchLose';
        subPath = "/bill/blankvou/bvvouinfodtl/batchLose";
        handle1(lang,len,rowData,subPath);
        //批量作废-已入库、已退票、已丢失状态才能操作
    }else if(operate=="batchTovoid"){
        for(i=0;i<len;i++){
            if(rowData[i].vouStatus!=01&&rowData[i].vouStatus!=04&&rowData[i].vouStatus!=05){//01 已入库 、04已退票、05已丢失
                layer.alert(bt.lang('bill.blankvou.bvvouInfoDtl.batchOperate03'), {icon: 7});
                return;
            }
        }
        lang = 'common.message.confirm.batchTovoid';
        subPath = "batchTovoidPage";
        handle2(len,rowData,subPath);
        //批量退票-已使用状态才能操作
    }else if(operate=="batchRetreat"){
        for(i=0;i<len;i++){
            if(rowData[i].vouStatus!=03){//03 已使用
                layer.alert(bt.lang('bill.blankvou.bvvouInfoDtl.batchOperate04'), {icon: 7});
                return;
            }
        }
        lang = 'common.message.confirm.batchRetreat';
        subPath = "batchRetreatPage";
        handle2(len,rowData,subPath);
        //批量移库-需要选择移库库存,已入库状态才能操作
    }else{
        for(i=0;i<len;i++){
            if(rowData[i].vouStatus!=01){//01 已入库
                layer.alert(bt.lang('bill.blankvou.bvvouInfoDtl.batchOperate01'), {icon: 7});
                return;
            }
            if(rowData[i].sboxId == $('#transfSboxId option:selected').val()){
                //选择的第{0}条数据的库箱和移库库箱相同!
                layer.alert(bt.lang('bill.blankvou.bvvouInfoDtl.repeat',[(i+1)]), {icon: 7});
                return;
            }
        }
        if($('#transfSboxId option:selected').val()==0){
            layer.alert(bt.lang('common.message.confirm.transfSboxId'), {icon: 7});
            return;
        }
        lang = 'common.message.confirm.batchTransf';
        subPath = "/bill/blankvou/bvvouinfodtl/batchTransf/"+$('#transfSboxId option:selected').val();
        handle1(lang,len,rowData,subPath);
    }
}

//批量-出售、丢失、移库
function handle1(lang,len,rowData,subPath){
    $ = bt.jquery;
    parent.layer.confirm(bt.lang(lang),function(){
        var ids;
        for(i=0;i<len;i++){
            if(ids){
                ids=ids+","+rowData[i].id;
            }else{
                ids=rowData[i].id;
            }
        }
        $.post(basePath + subPath,{"ids":ids}, function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
            }
        },'JSON');
    });
}

//批量-退票、作废
function handle2(len,rowData,subPath){
    var ids;
    for(i=0;i<len;i++){
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
    }
    var flag = true;
    parent.layer.open({
        type: 2,
        area: ['850px', '249px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvvouinfodtl/"+subPath+"/" + ids,
        cancel: function(index, layero){
            flag = false;
        },
        end:function(){
            if(flag){
//        		billTable.draw();
                bt.datatable.refreshCurrentPage();
            }
        }
    });
}

//单位回调获取银行账户、银行类别、库箱
function corpCallbackFun(ids){
    var $ = bt.jquery,form = bt.form();
    if(ids){
        //库箱
        bt.select.init({
            elem:'.selectSboxId',
            name:"eqs_sboxId",
            valueField:"id",
            textField:"name",
            placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
            search: true,
            disabled: false,
            url: basePath + "/bill/blankvou/bvstoragebox/jsonListByCorpIds/" + ids
        });
        ids = ids.split(",");
        //银行类别
        bt.select.init({
            elem:'.selectBankId',
            name:"eqs_bankName",
            valueField: "bankName",
            textField: "bankName",
            placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
            search: true,
            disabled: false,
            url: basePath + "/bill/blankvou/bvvouinput/getBankAccListByCorpIds/" + ids
        });
    }
}

//重置
function resetUpdate(){
    init();
}

//所属银行、所属库箱加载
function init(){
    //所属库箱
    bt.select.init({
        elem:'.selectSboxId',
        name:"eqs_sboxId",
        valueField:"id",
        textField:"name",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvstoragebox/selectData"
    });
    //下拉框--加载所属银行
    bt.select.init({
        elem:'.selectBankId',
        name:"eqs_bankName",
        valueField: "bankName",
        textField: "bankName",
        placeholder:{value:' ',text:bt.lang('common.placeholder.all')},
        search: true,
        disabled: false,
        url:basePath+"/bill/blankvou/bvvouinput/getBankAccList/json"
    });
}