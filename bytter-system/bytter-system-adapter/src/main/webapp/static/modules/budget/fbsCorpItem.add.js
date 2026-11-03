bt.use(['form','layedit','corp','selectTree','treeTable','jquery'], function(){
    var $ = bt.jquery,form = bt.form();
    
    bt.treeTable.init({elem:"#corpItem",column:1});

    bt.corpId = function(id) {
        checkBox(id);
	}
    bt.corpFont = function(treeId, treeNode) {
    	//1、先通过jquery获取页面上的corpId值，使用英文状态下的逗号隔开
    	//2、将获取到的值使用indexOf方法判断是否存在，如果存在，则返回{color:"red"}对象
    	var isItem = $("#itemCorp").val();
		if(isItem.indexOf(treeNode.id)>-1){
			return {color : ""};
		}else{
			return {background : "#81A5ED"};//未分配单位高亮显示
		}
    }

    bt.clearValue = function () {

        $(".itemCodes").prop("checked",false);
        form.render('checkbox')
    }
    bt.corp.init();
    
    form.on('checkbox(checkAll)', function(data){
    	var checked = data.elem.checked;
    	var checkboxs = $("#corp-table").children("tbody").find("input[type='checkbox']");
    	bt.each(checkboxs,function(){
    		
    		$(this).prop("checked",checked)
    		
    	})
    	form.render("checkbox")
  	});
    
    //监听提交
    form.on('submit(submit)', function(data){
    	var itemCodes = [];
    	$('form').find('input[name="itemCodes"]:checked').each(function(){
    		itemCodes.push($(this).val());
    	});
    	if(itemCodes.length > 0){
    		data.field.itemCodes = itemCodes;
    	}else{
    		itemCodes.push("");
    		data.field.itemCodes = itemCodes;
            parent.layer.alert(bt.lang('budget.message.itemcode.notassign'));
            return false;
    	}
        var action = "/budget/fbscorpitem/save";
        $.post(basePath + action,data.field,function(result){
        	if (result.success) {
				parent.layer ? parent.layer.closeAll() : layer.closeAll();
			}
			parent.layer ? parent.layer.alert(result.msg, {
				icon : result.success ? 1 : 2
			}) : layer.alert(result.msg, {
				icon : result.success ? 1 : 2
			});
			location.reload();
        },'JSON');
        return false;
    });
    unAll();


    function setChild(cid,checked){
    	if(cid){
    		var childTr =  $("table tr").find('tr[data-tt-parent-id="'+cid+'"]');
            childTr.each(function(data,dom){
                var childId = $(dom).data("tt-id");
                if(childId){
                    $(childTr).find("input").prop("checked",checked);
                    form.render("checkbox");
                }
                var childChildTr = $("table tr").find('tr[data-tt-parent-id="'+childId+'"]');
                if(childChildTr.length !==0){
                    setChild(childId,checked)
                }
            })
    	}
    }
    
    function setParent(pid,checked){
    	if(pid){
    		var childTrs = $("table").find('tr[data-tt-parent-id="'+pid+'"]');
			$('table tr[data-tt-id="'+pid+'"]').find("input").prop("checked",true)
  	 	 	form.render("checkbox");
    	}
		var id = $("table").find('tr[data-tt-id="'+pid+'"]').data("tt-parent-id");
		var parentTr = $("table").find('tr[data-tt-id="'+id+'"]')
		if(parentTr.length !==0){
			setParent(id,checked)
		}
    }
    form.on("checkbox(itemCorp)",function(data){
  	  //子科目的checkbox选中时，全部被选中或不选中
    	var checked = data.elem.checked;
 	    var cid = $(data.elem).parents("tr").data("tt-id");
	 	var pid = $(data.elem).parents("tr").data("tt-parent-id");
	 	  setParent(pid,checked)
	 	  //选中父科目子科目选中
	 	  setChild(cid,checked);
    })
    //点击全选按钮
  $("#checkAll").on("click",function(){
	 if($(this).hasClass("all")){
		 $(this).removeClass("all");
		 $(this).text(bt.lang('common.check.unAll'))
		 $("table").find('input[lay-filter="itemCorp"]').prop("checked",true);
	 }else{
		 $(this).addClass("all");
		 $(this).text(bt.lang('common.check.all'))
		 $("table").find('input[lay-filter="itemCorp"]').prop("checked",false);
	 }
	  form.render("checkbox")
  })

	//判断全选中提交时按钮变全不选
	function unAll() {
    	var size = $("table").find('input[lay-filter="itemCorp"]').length;
    	var checkSize = $("table").find('input[lay-filter="itemCorp"]:checked').size();
    	if(size == checkSize){
            $("#checkAll").removeClass("all");
            $("#checkAll").text(bt.lang('common.check.unAll'))
		}
    }

    /**
	 * 重绘科目树
     * @param id
     */
    function checkBox(id) {
    	var url = basePath + "/budget/fbscorpitem/getCheckItem";
		var isBatch = "";//默认单个
        if(id && id.indexOf(",") == -1){
            isBatch = "0";
        }else if(id){
            isBatch = "1";
        }else{
            $(".itemCodes").prop("checked",false);
            form.render('checkbox')
		}
        if(isBatch){
            $.ajax({
                url: url,
                method: 'POST',
                data: {"ids":id,"isBatch":isBatch},
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        if(result.obj){
                            $("#innerTree").html('<table class="layui-table" id="corpItem"><tbody>'+htmlEscape(result.obj)+'</tbody></table>')
                            bt.treeTable.init({elem:"#corpItem",column:1});
                            form.render('checkbox');
                        }else{
                            $("#innerTree").html('');
                        }
                    }
                }
            });
        }
    }
});
