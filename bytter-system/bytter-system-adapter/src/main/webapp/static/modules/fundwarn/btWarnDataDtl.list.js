var corp,dicts,dateType,warnDataTable,selectType;
bt.use(['form','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    initDataTable();
});

var getDataField=function(){
	var typeCode=$("#typeCode").val();
	var dimensionCode=$("#dimensionCode").val();
	var action = "/fundwarn/btwarndata/warnDataHeaderDtl";
	var dataField={};
	bt.jquery.ajax({
		type: "GET",
		url: basePath + action,
		data: {"typeCode":typeCode,"dimension":dimensionCode},
		dataType:"json",
		async: false,
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		success: function(result){
			dataField = result;
		},
		error: function(res){
		}
	});
	return dataField;
}

var getDataColumns=function(dataField){
	var dataColumn=new Array();
	if(dataField&&dataField.length>0){
		for(i=0;i<dataField.length;i++){
			var field={};
			if("num"==dataField[i].type){
				field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
	                    render: function ( data, type, full, meta ) {
	                    	return bt.money.format(data,{separator: "",decimal: 0});
	                    }
	                }
			}else if("money"==dataField[i].type){
				field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                    	return bt.money.format(data);
                    }
                }
			}else if("query"==dataField[i].type){
				field={data:null,bSortable:false,title:bt.lang(dataField[i].text),className:'center',render: function (data, type, full, meta) {
			         var html='<a class="" href="#" data-row="'+meta.row+'" onclick="viewDtl(this)">'+bt.lang("common.button.view")+'</a>';
			         return html;
			     }}
			}else{
				field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true};
			}
			dataColumn[i]=field;
		}
	}
	return dataColumn;
}

var initDataTable=function(){
	if(warnDataTable){
        $('#appTable').DataTable().destroy();
        $('#appTable').html('');
    }
	var dataField=getDataField();
	var dataColumns=getDataColumns(dataField);
    warnDataTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax:{
            'url': basePath+"/fundwarn/btwarndata/datadtltable"
        },
        columns:dataColumns 
    });
}
