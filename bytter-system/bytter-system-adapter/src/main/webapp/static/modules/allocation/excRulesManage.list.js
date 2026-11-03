var datatable;
bt.use(['form','datatable','select','dicts','corp','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    bt.bank.init({ 
    	elem:'.bank'
    })
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        checkbox:true,
        order: [[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/allocation/excrulesman/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('common.button.batchDelete'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    if(data.length < 1){
                    	layer.msg(bt.lang('common.message.confirm.selectData'));
                    	return;
                    }
                    var ids="";
                 	var rowData = data.splice(0,data.length);
                 	for(var i=0;i<rowData.length;i++){
                 		if (ids == "") {
    						ids += rowData[i].id;
    					} else {
    						ids += "," + rowData[i].id;
    					}
                 	}
                 	parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
                 	 $.post(basePath + "/allocation/excrulesman/batchDelete/",{ids:ids}, function(result){
                         bt.datatable.refreshCurrentPage();
                         parent.layer.alert(result.msg, {icon: result.success?1:2});
                     },'JSON');
                 	});
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
                  {data:"filterName",bSortable:true,className:'center'},
                  {data:"code",bSortable:false,className:'center'},
                  {data:"corpName",bSortable:true,className:'center',},
                  {data:"payeeAccNo",bSortable:true,className:'center'},
                  {data:"payerAccNo",bSortable:true,className:'center'},
                  {data:"validSign",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("sf_yn",full.validSign);
                      }
                  },
                  {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                      var html ='';
                      if(bt.isResource('netbank:allocation:excRulesManage:apply')){
                          html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                          html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                      }
                  	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                      return html;
                  }}
        ]
    });
  
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/allocation/excrulesman/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/excrulesman/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/excrulesman/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/allocation/excrulesman/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/excrulesman/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/allocation/excrulesman/delete/" + data.id, function(result){
        	 bt.datatable.refreshCurrentPage();
             parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}