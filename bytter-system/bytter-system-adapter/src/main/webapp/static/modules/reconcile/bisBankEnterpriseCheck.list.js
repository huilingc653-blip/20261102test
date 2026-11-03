var datatable={},mulselect,corp;
bt.use(['form','selectTable','datatable','moneyType','layedit','btdate','corp','selectType','dateType','element','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    mulselect=bt.mulselect.init();
    corp=bt.corp.init();

//    $("#accNo").css('display','none'); 

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bis/bankEnterprise/datatable"
        },
        buttons: [
            {
                text: bt.lang('余额相符'),
                hide:bt.isnotResource('bis:bank:enterprise:check:query'),
                action: function (e, dt, node, config ) {
                    optSure(dt.rows('.selected').data(),"1");
                }
            },
            {
                text: bt.lang('余额不符'),
                hide:bt.isnotResource('bis:bank:enterprise:check:query'),
                action: function (e, dt, node, config ) {
                    optSure(dt.rows('.selected').data(),"2");
                }
            },
            {
                text: bt.lang('调节相符'),
                hide:bt.isnotResource('bis:bank:enterprise:check:query'),
                action: function (e, dt, node, config ) {
                    optSure(dt.rows('.selected').data(),"3");
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"bankTypeName",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"accNature",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"billNo",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"toBillDate",bSortable:true},
            {data:"bal",bSortable:true},
            {data:"voucherStatName",bSortable:true}
        ]
    });


    $("#manual").click(function(){
        var bean = new Object();
        if(corp.get("corpId").val()==""){
            //请选择所属单位
            parent.layer.msg(bt.lang("accebill.message.corpid.isnotselect"), {icon: 2});
            return false;
        }else{
            bean.corpId = corp.get("corpId").val();
        }

        if($('input[name="ins_bankTypeId"]').val()==""){
            //请选择所属单位
            parent.layer.msg(bt.lang("nis.personneldetail.message.cardtypename.null"), {icon: 2});
            return false;
        }else{
            bean.bankTypeId =$('input[name="ins_bankTypeId"]').val();
        }

        if($('#dateStart').val()==null
            || $('#dateEnd').val()==null){
            parent.layer.msg(bt.lang("fbs.budget.message.querydate.null"), {icon: 2});
            return false;
        }else{
            bean.dateStart = $('#dateStart').val();
            bean.dateEnd = $('#dateEnd').val();
        }

        $.post(basePath+"/bis/bankEnterprise/manualExc",bean, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');

    });


    var selectTableObj = null;

    var bindBankplugIn = function(){
        $("#accNo").val('');
        $("#accId").val('');
        $('#accName').val('');
        $('#accBankName').val('');
        if(selectTableObj){
            selectTableObj.get('selectTable').reload({
                corpId: corpId,
                isVirtualAcc:dicts.get('isVirtualAcc').val()
            })
            return;
        }
        selectTableObj = bt.selectTableType.init({
            elem:".rasBank",
            url:basePath+"/bankacc/btbankacc/getRasBankListBy",
            scrollX:true,
            scrollY:"300px",
            paging:false,
            urldata: {corpId: corpId,isVirtualAcc:dicts.get('isVirtualAcc').val()},
            columns:[
                {data:"bankAcc",title:bt.lang('table.accountDTO.bankAcc')},
                {data:"accName",title:bt.lang('table.accountDTO.accName')},
                {data:"bankName",title:bt.lang('table.accountDTO.bankName')},
                {data:"curName",title:bt.lang('table.accountDTO.curName')},
            ],
            clickCallback:function(sdata,$tr){
                $("#accNo").val(sdata.bankAcc);
                $("#accId").val(sdata.bankAccId);
                $('#accName').val(sdata.accName);
                $('#accBankName').val(sdata.bankName);
                if(dicts.get('isVirtualAcc').val()=="1"){
                    $('#entityAccNo').val(sdata.entityAccNo);
                    $('#entityAccId').val(sdata.entityAccId);
                }
            },
            tableInitCallback:function(full,$elem){

            },
        })
    }

});


function optSure(data,opt){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //校验数据权限
    var rowData = data.splice(0,len);
    var ids;
    for(i=0;i<len;i++){
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
    }
    var url = basePath+"/optContrast/"+opt+"/"+ids;
    parent.layer.open({
        type: 2,
        area: ['850px', '249px'],
        maxmin: true,
        content:url,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });

}

//业务种类初始化前回调
var busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
