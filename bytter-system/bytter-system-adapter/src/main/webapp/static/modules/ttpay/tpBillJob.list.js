var billTable,selectTree,$,form,dicts,corp;
bt.use(['form','corp','datatable','dicts','selectTree','mulselect','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    selectTree=bt.selectTree.init({
        url:basePath+"/ttpay/merchant/treeData",
        elem:".merchantNo-selectTree-checkbox",
        valueField:'merchantNo',
        name: 'ins_merchantNo',
        checkType:'check'
    });
    
    initMerchantNoTree = function () {
    	var corpId=corp.get('corpId').val();
    	var payType=dicts.get('payType').val();
    	//selectTree.setUrl(basePath + "/ttpay/merchant/treeData?corpId="+corpId+"&payType="+payType);
    }
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        isInitData: false,
        order: [[2,'asc'],[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ttpay/billJob/datatable"
        }, 
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true,
                render:function(data, type, full, meta){
                	var corp=full.corpName;
                	if(full.corpCode){
                		corp="("+full.corpCode+")"+corp
                	}
                    return corp;
                }
            },
            {data:"merchantNo",bSortable:true},
            {data:"payTypeName",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"billDate",bSortable:true},
            {data:"createTime",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if(full.status==1){
            		html+='<a class="" href="#" data-row="'+meta.row+'" onclick="dtlView(\''+full.billDate+'\',\''+full.payType+'\',\''+full.appId+'\')">'+bt.lang("common.button.dtl.view")+'</a>';
            	}
            	return html;
            }}
        ]
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
    bt.dateType.init();
});

//跳转至添加页面 
function dtlView(billDate,payType,appId){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/ttpay/payBillDtl/dtlView?billDate="+billDate+"&payType="+payType+"&appId="+appId);
        return;
    }
	parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/payBillDtl/dtlView?billDate="+billDate+"&payType="+payType+"&appId="+appId,
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}


