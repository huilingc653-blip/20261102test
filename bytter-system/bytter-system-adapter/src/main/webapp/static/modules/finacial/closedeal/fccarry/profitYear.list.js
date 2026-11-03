bt.use(['form','datatable','btdate','corp','date','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    var dateFun = bt.dateType.getFun();
    //单位回调函数
    bt.corpCallback = function (id) {
        if(id) {
            //获取单位信息
            $.ajax({
                url: basePath + "/finacial/closedeal/fccarry?getCarryDate",
                method: 'POST',
                data: {netId: id},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        if (data.obj.carryTime) {
                            $('#startDateInfo').text(bt.lang('finacial.closedeal.fccarry.profitYearInfo')+dateFun.format(data.obj.voucherEndDate, 'yyyy-MM-dd'));
                        }
                    }else{
                        $('#startDateInfo').text('');
                        $('#startDate').val('');
                    }
                    form.render();
                }
            });
        }
    }
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            initQueryParams:function (params) {
                // name="eqs_netId"
                var eqsNetId = params.eqs_netId;
                if(eqsNetId == '' || eqsNetId == null){
                    params['eqs_netId']= $("#eqs_netId_id").val()
                }
                return params
            }
        },
        ajax: {
            'url': basePath+"/finacial/closedeal/fccarry/findProfitYear"
        },
        buttons: [
        ]
        ,columns: [
            {data:"account",bSortable:false,className:'center'},
            {data:"accName",bSortable:false,className:'center'},
            {data:"curName",bSortable:false,className:'center'},
            {data:"balance",bSortable:false,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(full.balance);
            	}
            },
            {data:"balanceWay",bSortable:false,className:'center',
            	render: function ( data, type, full, meta ) {
            		return bt.dict("Admin.BalanceWay",full.balanceWay);
            	}
            }
        ]
    });

    /**
     * 查看结转凭证
     */
    profitViewClose = function(){
    	alert('11');
    }



});

//查看结转或打回凭证
function viewProfitYear(voucherState){
	var netId = corp.get("netId").val();
	var param = "eqs_netId=" + netId + "&voucherState=" + voucherState;
	var url = basePath+"/finacial/closedeal/fccarry/viewProfitYear?"+param
    if ("F" == voucherState) {
        var module1 = parent.layer.open({
            type: 2,
            area: ['1200px', '700px'],
            maxmin: true,
            content: url,
            btn: [bt.lang('Finacial.accbook.common.button.add'), bt.lang('common.button.delete'), bt.lang('common.button.close')],
            btnAlign: "c",
            yes: function (index, layero) {
                param1 = {};
                param1["eqs_netId"] = netId;
                param1["voucherState"] = voucherState;
                // 再次提交复核
                rebackProfitYear(basePath + "/finacial/closedeal/fccarry/rebackCheckProfitYear", param1);
                parent.layer.close(index); //再执行关闭
            },
            btn2: function(index, layero) {
                param1 = {};
                param1["eqs_netId"] = netId;
                param1["voucherState"] = voucherState;
                // 删除损益结转
                rebackProfitYear(basePath + "/finacial/closedeal/fccarry/deleteProfitYear", param1);
                parent.layer.close(index); //再执行关闭
            },
            btn3: function(index, layero) {
            },
            end: function () {
                datatable && datatable.draw();
            }
        });
    } else {
         parent.layer.open({
            type: 2,
            area: ['1200px', '700px'],
            maxmin: true,
            content: url,
            btn: [bt.lang("common.button.close")],
            btnAlign: "c",
            btn2: function (index, layero) {
                console.info("btn2");
                layer.close(index);
            },
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/netbank/nisbillhead/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

/**
 * 记账操作
 * @returns {boolean}
 */
function doProfitYear() {
    if(!bt.form().verifyFun('#searchForm')) {
        return false;
    }
    var url = basePath + "/finacial/closedeal/fccarry/submitProfitYear";
    bt.loading.show();
    $.ajax({
        url : url,
        data : $("#searchForm").serialize(),
        dataType : "json",
        type : "POST",
        success : function (data) {
            parent.layer?parent.layer.alert(data.msg, {icon: data.success?1:2}):layer.alert(data.msg, {icon: data.success?1:2});
            if (data.success){
                if(!bt.navtab){
                    bt.navtab.refreshCurrentTable();
                } else {
                    bt.loading.hide();
                }
            }else{
                bt.loading.hide();
            }
        }
    })
}

/**
 * 打回数据再次提交
 * @param url
 * @param sendData
 * @returns {boolean}
 */
function rebackProfitYear(url, sendData) {
    if (sendData.length <= 0) {
        return false;
    }
    bt.loading.show();
    $.ajax({
        url : url,
        data : sendData,
        dataType : "json",
        type : "POST",
        success : function (data) {
            parent.layer?parent.layer.alert(data.msg, {icon: data.success?1:2}):layer.alert(data.msg, {icon: data.success?1:2});
            if (data.success){
                if(!bt.navtab){
                    bt.navtab.refreshCurrentTable();
                } else {
                    bt.loading.hide();
                }
            }else{
                bt.loading.hide();
            }
        }
    });
}

function beforeInitNetId(nodes) {
    $("#eqs_netId_id").remove()
    return nodes
}
