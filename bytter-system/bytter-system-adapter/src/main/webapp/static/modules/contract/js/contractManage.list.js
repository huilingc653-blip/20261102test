bt.use(['datatable', 'jquery', 'corp','element','dicts'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var element = bt.element();
    var dataform = bt.dataform;
    bt.dicts.init();
    var $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();

    //权限控制共享非共享
    element.on('tab(externalCorpTabBrief)', function(data){
        var status = $(this).hasClass('tab-type') ? 1 : 0;
        $("input[name='isCommon']").val(status==1?"common":"corp");
        if(status==1){
            bt.corp.get("corpId").val("");
        }
        showOrHiddenCorp(status);
        table.draw();
    });
    var table = bt.datatable.init({
        elem: "#externalCorpTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1,'asc']],
        searchForm: {
        	formElem: "#searchForm",
        	formFilter: "search"
        },
        exportFile:{
//        	printTitle:function(){
//        		return $('.layui-this').text();
//        	},
        	outputTitle:function(){
        		return $('.layui-this').text();
        	},
        	outputFilename: function(){
        		return $('.layui-this').text();
        	},
        },
        ajax: {
            'url': basePath + "/contract/contractType/datatable.json",
        },
        buttons: [
            {
                text: bt.lang("common.button.add"),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
            	extend: "colvis",
                text: bt.lang('common.button.theadHide'),//'列隐藏',
            }
        ],
//        ignoreButtons:['colvis','output','print']
//        ,
        columns: [
            {data: "typeCode",bSortable:true,className: 'center'},
            {data: "name",bSortable:true,className: 'center'},
            {data: "rmk", orderable: false,className: 'center'},
            {data: "validSign", orderable: false,className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.validSign);
                }
            },
            {data: "shareSign", orderable: false,className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.shareSign);
                }
            },
			{
                data: "corpId",bSortable:true,className: 'center',
                render: function (data, type, full, meta) {
                	if(full.corpCode==null || full.corpName==null){
                		return "";
                	}else{
                		return "(" + full.corpCode + ")" + full.corpName;
                	}
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if ((full.auditStatus != 11)) {
                        html += '<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    if ((full.delFlag =="0")) {
                    	html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        fixedColumns:{
        	rightColumns:1
        }
    });

    //新增跳转
    function add() {
        var url = basePath + "/contract/contractType/add?power="+$("input[name='isCommon']").val();
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
                datatable && datatable.draw();
            }
        })
    }


    initLoadPower();
    view = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        var url = basePath + "/contract/contractType/view?id=" + data.id;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), url);
            return;
        }
    }

    update = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            var url = basePath + "/contract/contractType/update?id=" + data.id + "&parms=" + $("input[name='isCommon']").val();
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.update.data'), url);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: url,
                end: function () {
                    datatable && datatable.draw();
                }
            });
        }
    }

    //删除方法
    deleteColumn = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            var ids = [data.id];
            parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                $.post(basePath + "/contract/contractType/del", {externalIds: ids}, function (result) {
                    location.reload();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        }
    }

    function showOrHiddenCorp(value) {
        /*Tab为共享单位时,隐藏单位列*/
        var column = table.column(6);
        if (value == 1){
            column.visible(false);
            $(".inlineCorp").attr("style", "display:none");
        }else{
            column.visible(true);
            $(".inlineCorp").attr("style", "display:inlineblock");
        }
    }
    
    //页面加载调用
    function initLoadPower(){
    	var power = $("input[name='isCommon']").val();
    	if(power=="corp"){
    		 $('[data-status="1"]').removeAttr("class", "layui-this tab-type");
    		 $('[data-status="1"]').attr("style", "display:none");
    		 $('[data-status="0"]').attr("class", "layui-this tab-type");
    		  showOrHiddenCorp(0);
    	}
    	if(power=="common"){
    		 $('[data-status="0"]').removeAttr("class", "layui-this tab-type");
   		 	 $('[data-status="0"]').attr("style", "display:none");
   		 	 $('[data-status="1"]').attr("class", "layui-this tab-type");
   		 	 showOrHiddenCorp(1);
    	}
    	if(power=="all"){
    	    showOrHiddenCorp(1);
    	    $("input[name='isCommon']").val("common");
    	}
    } 
    
});
