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
        url: urlData,
        buttons: [
        ]
        ,columns: [
            {data:"loanCorp",bSortable:false,className:'center'},
			{data:"billCode",bSortable:false,className:'center'},
			{data:"loanDate",bSortable:false,className:'center'},
			{data:"curName",bSortable:false,className:'center'},
			{data:"loanSendOutMoney",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data.loanSendOutMoney);
			}},
			{data:"loanSendOutDate",bSortable:false,className:'center'},
			{data:"repayDate",bSortable:false,className:'center'},
			// {data:"repayType",bSortable:false,className:'center',
            // 	render: function ( data, type, full, meta ) {
            //         return bt.dict("cms.LoanRepayType",data.repayType);
            //     }
            // },
            
            {data:"repayTypeName",bSortable:false,className:'center'},
			{data:"loanClass",bSortable:false,className:'center',
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.LoanClass",data.loanClass);
                }	
            },
			{data:"loanTerm",bSortable:false,className:'center',
            	render: function ( data, type, full, meta ) {
            		if(data.ternUnit==2){
            			return data.loanTerm + bt.lang("cms.loan.ternUnit.day");
            		}else if(data.ternUnit==1){
            			return data.loanTerm + bt.lang("cms.loan.ternUnit.month");
            		}else{
            			return data.loanTerm;
            		}
                }	
            }
        ]
    });
});


