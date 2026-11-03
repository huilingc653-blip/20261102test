bt.use(['form','datatable','corp','bttable','money'], function(){
    $ = bt.jquery,form = bt.form();
    var urlData = basePath + "/finacial/closedeal/fcdayclose/view" + $("#checkType").val() 
			+ "?netCode=" + $("#netCode").val()  + "&closeDate=" +  $("#closeDate").val()
			+ "&lastCloseDate=" + $("#lastCloseDate").val();
    
    //生成列表
    bt.bttable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        order: [1,'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        url : urlData,
        buttons: [
        ]
        ,columns: [
            {data:"account",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"balance",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(data.balance);
	            }
	        },
            {data:"cumulationa",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(data.cumulationa);
	            }
	        },
            {data:"yearBalance",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(data.yearBalance);
	            }
	        },
            {data:"yearTotal",bSortable:true,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(data.yearTotal);
	            }
	        },
	        {data:"operator",bSortable:false},
            {data:"status",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("common.status",data.status);
                }
            }
        ]
    });
});


