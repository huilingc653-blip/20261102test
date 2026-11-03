 var billTable={},corp;
    bt.use(['form','datatable','moneyType','billPool','btdate','billPoolCorp','select','mulselect','dateType','corp','dicts','selectType'],function(){
        $ = bt.jquery,form = bt.form();
        bt.billPoolCorp.init();
        bt.dicts.init();
        bt.moneyType.init();
        bt.billPool.init();
        bt.corp.init();
        bt.dateType.init();
        bt.selectType.init();
        bt.mulselect.init();
        //生成列表
        billTable = bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            colReorder: true,
            checkbox:true, 
            order: [[7,'desc']],
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath+"/billPool/outApply/datatable"
            },
            buttons: [
                {
                    text: bt.lang("billPool.outapply.apply"),
                    action: function (e, dt, node, config) {
                        abpoolApple(dt.rows('.selected').data());
                    }
                },
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),//'列隐藏',
                }
            ]
            ,columns: [
                {data:"corpName",bSortable:true,
                    render: function ( data, type, full, meta ) {
                       return '('+full.corpCode+')'+full.corpName;
                    }
                },
                {data:"briefId",bSortable:true,className:'center',
                    render: function ( data, type, full, meta ) {
                        return bt.dict("AB_BILL_INSBRIEFID_TEST",full.briefId);
                    }
                },
                {data:"origNo",bSortable:true},
                {data: "billType", bSortable: true,className:'center',
                    render: function ( data, type, full, meta ) {
                        return bt.dict("AB_BILL_TYPE",full.billType);
                    }
                },
                {data: "billFlag", bSortable: true,className:'center',
                    render: function ( data, type, full, meta ) {
                        return bt.dict("AB_BILL_FLAG",full.billFlag);
                    }
                },
                {data:"billMoney",bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.money.format(full.billMoney);
                    }
                },
                {data:"startDate",bSortable:true},
                {data:"endDate",bSortable:true},
                {data:"payer",bSortable:true},
                {data:"payee",bSortable:true},
                {data:"acceptor",bSortable:true},
                {data:"abPoolName",bSortable:true},
                {data:"poolSatatus",bSortable:true,
                    render: function ( data, type, full, meta ) {
                        return bt.dict("AB_BILL_POOL_STATUS",full.poolSatatus);
                    }
                },
                {data:"status",bSortable:true,
                    render: function ( data, type, full, meta ) {
                        return bt.dict("AB_POOL_STATUS",full.status);
                    }
                },
                {data:"inoutType",bSortable:true,
                    render: function (data, type, full, meta) {
                        return bt.dict("AB_POOL_INOUT_TYPE",full.inoutType);
                    }},
                {data:"createTime",bSortable:true},
                {data:"optStatus",bSortable:true,
                    render: function ( data, type, full, meta ) {
                        return bt.dict("AB_POOL_DTL_OPT_STATUS",full.optStatus);
                    }
                },
                {data:"returnMsg",bSortable:true},
                {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {

                        var html='';
                        if(data.optStatus==-2 || data.optStatus==-1 || data.optStatus==-4){
	                        html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,0)">'+bt.lang("billPool.intoApply.continue")+'</a>';
	                        html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="setting(this)">'+bt.lang("billPool.sign.stop")+'</a>  | ';
                    	}
                    	html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    	return html;
                    }}
            ],
            fixedColumns:{
                rightColumns:1
            }
        });

    });

    //查看详细
    function view(el){
        var data =  billTable.row(layui.jquery(el).data("row")).data();
        parent.layer.open({
	        type: 2, 
	        area: ['1275px', '750px'],
	        maxmin: true,
	        content: basePath + "/billPool/pdtst/view/" + data.apdId
    	});  
    }
   	//终止
    function setting(el){
        var data =  billTable.row(layui.jquery(el).data("row")).data();
        var msg ="billPool.sign.isStop";
        parent.layer.confirm(bt.lang(msg),function(){
            $.post(basePath + "/billPool/outApply/opt",{"id":data.apdId,"opt":0}, function(result){
                bt.loading.hide();
                if(result.success){
                    parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                    bt.datatable.refreshCurrentPage();
                }else{
                    parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                }
            },'JSON');
        });
    }
	//继续
    function operation(el,order){
        var data =  billTable.row(layui.jquery(el).data("row")).data();
        var msg ="billPool.into.continue.repeat.tips";
        parent.layer.confirm(bt.lang(msg),function(){
            $.post(basePath + "/billPool/outApply/opt",{"id":data.apdId,"opt":1}, function(result){
                bt.loading.hide();
                if(result.success){
                    parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                    bt.datatable.refreshCurrentPage();
                }else{
                    parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                }
            },'JSON');
        });
    }


	//入池申请
    function abpoolApple(data){
        var len = data.length;
        if(!len){
            layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
            return;
        }
        //校验数据权限
        var rowData = data.splice(0,len);
        var ids;
        var abCount = 0;
        var abBillMoney=0.00;
        var flag =rowData[0].billFlag;
        for(i=0;i<len;i++){
        	if(flag!=rowData[i].billFlag){
        		layer.alert(bt.lang('abpool.into.billFlag.msg'), {icon: 2}); 
        		return false;
        	}
            if(ids){
                ids=ids+","+rowData[i].apdId;
                abBillMoney=abBillMoney+bt.money.toNumber(rowData[i].billMoney);
                abCount++;
            }else{
                ids=rowData[i].apdId;
                abCount++;
                abBillMoney=bt.money.toNumber(rowData[i].billMoney);
            }
        }
        parent.layer.open({
            type: 2,
            area: ['885px', '459px'],
            maxmin: true,
            content: basePath + "/billPool/outApply/markeApple/" + ids+"?abBillMoney="+abBillMoney+"&abCount="+abCount+"&billFlag="+flag,
            end:function(){
                bt.datatable.refreshCurrentPage();
            }
        });

    }