bt.use(['form','datatable','btdate','moneynum','dateType','corp','dicts','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.corp.init();
    bt.dicts.init();
    bt.bank.init({
   	   elem:'.bank',    
    });
    
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:2});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        
        checkbox:false,order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/accbook/fcaccbookblned/editdataTable"
        },
        buttons: [
        ]
        ,columns: [
            {data:"busRaceId",bSortable:true,className:'center',render: function (data, type, full, meta) {
			    return full.raceName;
			}},
            {data:"voucherType",bSortable:true,className:'center',render: function (data, type, full, meta) {
			    return full.voucherName;
			}},
            {data:"tallyDate",bSortable:true},
            {data:"interestStartDate",bSortable:true},
            {data:"orderNo",bSortable:true},
            {data:"rmk",bSortable:false},
            {data:"money",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
			    return bt.money.format(data);
			}},
            {data:"voucherState",bSortable:false,render: function (data, type, full, meta) {
                return bt.dict("Finacial.VoucherState", data);
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html ="";
            	if(full.isOperation=="Y"){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
            		<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("Finacial.accbook.rush.button.edit")+'</a>\
            		<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("Finacial.accbook.rush.button.delete")+'</a>';
            	}
                return html;
            }}
        ]
    });
});
function add(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('Finacial.accbook.rush.button.add'),basePath + "/finacial/accbook/fcaccbookblned/add?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbookblned/add?id=" + data.id,
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
    });
}
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
        	bt.navtab.addNavtab(bt.lang('Finacial.accbook.rush.button.view'),basePath + "/finacial/accbook/fcaccbookblned/viewrush?id=" 
            		+ data.id+"&busGlobalId="+data.busGlobalId+ "&opType=check");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1000px', '600px'],
            maxmin: true,
            content: basePath + "/finacial/accbook/fcaccbookblned/viewrush?id=" 
    				+ data.id+"&busGlobalId="+data.busGlobalId + "&opType=check",
			end:function(){
	        	bt.navtab.refreshCurrentTable();
	        }
        });
    }
}
//冲账修改
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('Finacial.accbook.rush.button.edit'),basePath + "/finacial/accbook/fcaccbookblned/update?id=" 
            		+ data.id+"&busGlobalId="+data.busGlobalId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1000px', '600px'],
            maxmin: true,
            content: basePath + "/finacial/accbook/fcaccbookblned/update?id=" + data.id+"&busGlobalId="+data.busGlobalId,
            end:function(){
            	bt.navtab.refreshCurrentTable();
            }
        });
    }
}
//取消冲账
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/finacial/accbook/fcaccbookblned/delete?busGlobalId=" + data.busGlobalId +"&pageType=edit", function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}