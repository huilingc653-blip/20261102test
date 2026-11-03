bt.use(['form','datatable','corp','btdate','select','moneyType','selectType','dateType','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    selectType = bt.selectType.init();
    dateType = bt.dateType.init();

    bt.selectTable.init({
        elem:"#accountTable",
        url:basePath+"/finacial/closedeal/fcaccbalance/queryAccount",
        searchInput:".bankAccInput",
        scrollX:true,
        paging:true,
        scrollY:"300px",
        columns:[
                 {data:"blnctrAcc",},
	             {data:"accName",},
        ],
        clickCallback:function(severData,data){
            if(data){
                $('.bankAccInput').val(severData.blnctrAcc);
            }else{
                $('.bankAccInput').val('');
            }
        },
    }); 
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        ajax: {
            'url': basePath+"/finacial/closedeal/fcaccbalance/datatable"
        },
        searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },columns: [
            {data:"balanceDate",bSortable:true},
            {data:"account",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"balanceName",bSortable:true},
            {data:"yesterBalance",bSortable:true,className:"textRight",
            	render: function (data, type, full, meta) {
            		return moneyFun.format(full.yesterBalance);
            	}
            },
            {data:"debitMoney",bSortable:true,className:"textRight",
            	render: function (data, type, full, meta) {
            		return moneyFun.format(full.debitMoney);
            	}
            },
            {data:"creditMoney",bSortable:true,className:"textRight",
            	render: function (data, type, full, meta) {
            		return moneyFun.format(full.creditMoney);
            	}
            },
            {data:"balance",bSortable:true,className:"textRight",
            	render: function (data, type, full, meta) {
            		return moneyFun.format(full.balance);
            	}
            },
            {data:"subjectName",bSortable:true},
        ],summary:{
            pageSum:true,
            allSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'6',
                    dataName:'yesterBalance',
                },
                {
                    colNum:'7',
                    dataName:'debitMoney'
                },
                {
                    colNum:'8',
                    dataName:'creditMoney'
                },
                {
                    colNum:'9',
                    dataName:'balance'
                },
            ]
        },
    });
    
  /*  form.on('submit(excel)', function(data) {
    	var url = basePath + "/finacial/closedeal/fcaccbalance/excel" ;
    	DownLoadFile( { url : url , data : data.field , method : 'get' } ); 
    	return false;
  	}); */
    
});

function checkType(data){
	   var type = data.type;
	   if(type === 1){
		   $("#corpId").val(data.id);
		   $("input[name='eqs_netId']").val("");
	   }else{
		   $("#corpId").val("");
		   $("input[name='eqs_netId']").val(data.id);
	   }
};

/*function DownLoadFile (options) {
    var config = $.extend( true, { method: 'post' }, options );
    var $iframe = $( '<iframe id="down-file-iframe" />' );
    var $form = $( '<form target="down-file-iframe" method="' + config.method + '" />' );
    var url = config.url ;
    var data = config.data ;
    $form.attr( 'action' , url );
    for (var key in data ) {
        $form.append('<input type="hidden" name="' + key + '" value="' + data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
} 
		*/