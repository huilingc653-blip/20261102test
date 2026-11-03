bt.use(['form','datatable','moneynum','btdate','money','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect:false,
        checkbox:true,order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/finacial/accbook/accrue/fcchargeaccrue/datatable"
        },
        buttons: [
        ]
        ,columns: [
            {data:"account",bSortable:false},
            {data:"voucherNo",bSortable:false},
            {data:"accName",bSortable:false},
            {data:"voucherName",bSortable:false},
            {data:"chargeMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
                return bt.money.format(data);
            }},
            {data:"tallySign",bSortable:false,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcCharge.tallySign",full.tallySign);
                }
            }
        ]
    });
    
    //监听计提
    form.on('submit(charge)', function(data){
    	var rowSelect = layui.jquery("#appTable").DataTable().rows('.selected').data();
    	var len = rowSelect.length;
    	if (len < 1) {
    		layer.alert(bt.lang('table.fcBankBill.msg.chooseLeastOne'));
    		return false;
    	}
    	var ids = "";
    	var rowData = rowSelect;
    	for (var i = 0; i < rowData.length; i++) {
    		if (ids == "") {
    			ids += rowData[i].id;
    		} else {
    			ids += "," + rowData[i].id;
    		}
    	}
    	
    	var action = "/finacial/accbook/accrue/fcchargeaccrue/doChargeAccrue";
        $.post(basePath + action, "id=" +ids,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                /*if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {*/
                	
//                	bt.navtab.refreshParent();
                	bt.navtab.refreshCurrentTable();
                //}
            }
        },'JSON');
        return false;
    });

    //显示该账户下所有的计提数据
    function view(el){
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/finacial/accbook/accrue/fcchargeaccrue/view/" + data.account
        });
    }
});


