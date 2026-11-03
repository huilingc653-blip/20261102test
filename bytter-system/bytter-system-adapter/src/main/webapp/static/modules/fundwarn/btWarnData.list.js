var corp,dicts,dateType,warnDataTable,selectType;
bt.use(['form','datatable','corp','select','dicts','dateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    initDimensionCode = function (data) {
    	dicts.get('dimensionCode').attr({'data-displayData':typecodeDimensions[data.value]});
    };
    initWarnDataTable = function (data) {
    	var typeCode=dicts.get('typeCode').val();
    	var dimensionCode=dicts.get('dimensionCode').val()
    	if(typeCode && dimensionCode 
    		 && $.trim(typeCode) && $.trim(dimensionCode)){
			initDataTable();
    	}
    };
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    dateType=bt.dateType.init();
    selectType = bt.selectType.init();
    
});

var getDataField=function(){
	var typeCode=dicts.get('typeCode').val();
	var dimensionCode=dicts.get('dimensionCode').val()
	var action = "/fundwarn/btwarndata/warnDataHeader";
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
	console.log("dataColumn="+dataColumn);
	return dataColumn;
}

var initDataTable=function(){
	var dataField=getDataField();
	var dataColumns=getDataColumns(dataField);
    //生成列表
	if(warnDataTable){
        $('#appTable').DataTable().destroy();
        $('#appTable').html('');
    }
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
            'url': basePath+"/fundwarn/btwarndata/datatable"
        },
        columns:dataColumns 
    });
}

var viewDtl=function(el){
    var data = warnDataTable.row(layui.jquery(el).data("row")).data();
    var params="id=" + data.id + "&typeCode=" + data.typeCode + "&dimension=" + data.dimensionCode;
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/fundwarn/btwarndata/listDtl?" + params);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/fundwarn/btwarndata/listDtl?" + params
    });
}