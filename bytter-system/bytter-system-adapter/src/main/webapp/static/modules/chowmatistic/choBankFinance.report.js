var datatable;
bt.use(['form','datatable','select','selectTable','dateType','corp','money','moneyType','dicts','bank','rateType','date'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var rateType = bt.rateType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [[2,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/chobankfinance/dataTableReport"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data:"corpName",bSortable:true},
            {data:"busType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("choBankFinance.busTypeQuery",full.busType);
                }
            },
            {data:"billNo",bSortable:true,className:'center'},
            {data:"curName",bSortable:true},
            {data:"dayLimit",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return full.dayLimit+bt.dict("choBankFinance.dateUnit",full.dateUnit);
                }
            },
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payerBank",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.payMoney);
                }
            },
            {data:"interestRate",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return rateType.format(full.interestRate);
                }
            },
            {data:"referInterest",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.referInterest);
                }
            },
            {data:"interestStartDate",bSortable:true,className:'center'},
            {data:"deadlineDate",bSortable:true,className:'center'},
            {data:"maxWishPayday",bSortable:false,className:'center'},
            {data:"drawMoney",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.drawMoney);
                }
            },
            {data:"calEndDate",bSortable:false,className:'center'},
            {data:"calDays",bSortable:false,className:'textRight'},
            {data:"interestReceivable",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.interestReceivable);
                }
            },
            {data:"blInterest",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.blInterest);
                }
            },
            {data:"interestReceivableNot",bSortable:false,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.interestReceivableNot);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}
        ]
    });
    
    //付款账号
    var payerAccInit = bt.selectTable.init({
      	 elem: "#payerAccTable",
      	 url: basePath+"/chowmatistic/chobankfinance/getBankAcc",
      	 urldata: {corpId: $("input[name='corpId']").val(), busType: 10},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#payerAcc",
         paging:true,
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#payerAcc').val(data.bankAcc);
      			 $('#payerAccId').val(data.id);
      			 $('#curId').val(data.curId);
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });
    
    //单位回调函数
    bt.corpCallback = function(id){
    	//重新加载付款账号
    	payerAccInit.get("payerAcc").reload({corpId: id, busType: $("#busType").val()});
    	
    	//获取单位用途
    	$.ajax({
            url: basePath+"/basic/business/btpurpose?getPurpose",
            method: 'POST',
            data: {corpId: id,sysBusType:4},
            dataType: 'json',
            success: function(data){
            	$("#purpose").empty();
            	$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
            	if(data && data.obj &&  data.obj.length>0){
            		$.each(data.obj,function(index,item){
            			$("#purpose").append("<option value="+item.purpose+">"+item.purpose+"</option>");
            		});
            	}else{
            		$("#purpose").append("<option value=''></option>");
            	}
            	form.render();
            }
        });
    }
    
    //初始化单位
    bt.corp.init();
});

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/chobankfinance/viewQuery/" + data.id
    });
}
