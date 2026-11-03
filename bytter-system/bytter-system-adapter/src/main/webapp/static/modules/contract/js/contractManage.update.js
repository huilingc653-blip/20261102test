bt.biz = function (bizName, pluginName) {
    return window.biz[bizName][pluginName];
}
var sortRowNum=1;
var form;
var selectTypes;
bt.use(['form', 'jquery', 'corp','select','selectType', 'bttable', 'bankInfo', 'dicts', 'externalCorpBankAcc','selectTableType','swiftInfo'], function () {
    form = bt.form(), $ = bt.jquery;
    var layer = parent ? parent.bt.layer : bt.layer;
    //数据来源，用到字典
    bt.dicts.init();
    var corp = bt.corp.init();
    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    selectTypes = bt.selectType.init();//下拉框
    var bankInfo = bt.bankInfo.init();
    var swiftInfo = bt.swiftInfo.init();
    var bankAccTable;

    form.on('switch(checkCorp)', function(data){
        if (!this.checked) {
            $("#inputCorp").find('.select-tree-input').attr("lay-verify","required");
            $("#inputCorp").find('.select-tree-input').removeAttr("disabled");
            $(".celRow").show();
//            $("input[name='shareSign']").removeAttr("lay-verify");
        }else{
            $("#inputCorp").find('.select-tree-input').val('');
            $("#inputCorp").find('.select-tree-input').removeAttr("lay-verify");
            $("#inputCorp").find('.select-tree-input').attr("disabled","disabled");
            bt.corp.get("corpId").val("");
            $(".celRow").hide();
//            $("input[name='shareSign']").attr("lay-verify","required");
        }
        form.renderIcon();
    });

    if ($("#isCommonValue").val() !== "") {
        initCorpAndisCommon($("#isCommonValue").val());
    }

    form.on('submit(submitEvent)', function(data) {
        submitForm(data);
    });
    
    //取消按钮
    $("#cancel").click(function(){
    	bt.navtab.closeCurrent();
    	return false;
    });
    
    //提交方法
    function  submitForm(data) {
        //是否共享
        if (typeof(data.field.shareSign) == "undefined") {
            data.field.shareSign = 0;
        } else if (data.field.shareSign == "on") {
            data.field.shareSign = 1;
        } else {
            data.field.shareSign = 0;
        }
        
        //是否有效
        if (typeof(data.field.validSign) == "undefined") {
            data.field.validSign = 0;
        } else if (data.field.validSign == "on") {
            data.field.validSign = 1;
        } else {
            data.field.validSign = 0;
        }
        var action = $('input[name="id"]').val() ? "updateConType" : "save";
        $.post(basePath + '/contract/contractType/' + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2},function () {
                layer.closeAll();
                if (result.success) {
                    if(!bt.navtab){
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    }else{
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    }
                }
            });
        }, 'JSON');
        return false;
    }

    //点击swift后回调
    swiftInfoCallback=function(data,dom){
        var tr = dom.parents('tr');
        var tds = dom.parents('tr').children('td');;
        var index = tr.attr("row");
        tds.eq(1).find('textarea').val(data.bankName);
        tds.eq(2).find('input').val(data.swiftCode);
        tds.eq(4).find('input').val(data.cityName);
         $('#accProvince' + index + '').val(data.countryCode);
         $('#accProvinceFex' + index + '').val(data.nameZh);
    }

    var extenrnalId = $('input[name="id"]').val();
    var rowTr = 0;

    var addRow = function(index){
        var isCity = addBankAcc(index);
    }

    //国内账号表格新增行
    addBankAcc = function (index) {
        var html = '<tr row="' + index + '">';
        html += '<td style="text-align: center;"><input type="text" lay-verify="required" name="conTypeFieldVO[' + index + '].fieldName" autocomplete="off" class="layui-input externalAcc" /></td>';
        html += '<td style="text-align: center;"><textarea name="conTypeFieldVO[' + index + '].rmk" class="layui-textarea"></textarea></td>';
        html += '<td style=""><input type="checkbox" class="defaultAcc" name="conTypeFieldVO[' + index + '].requiredSign" lay-skin="switch" checked lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '"></td>';
        html += '<td style=""><div class="bt-biz-dict" data-defaultvalue="01" data-required="true" data-type="select" id="fieldType" name="conTypeFieldVO[' + index + '].fieldType" data-name="conTypeFieldVO[' + index + '].fieldType" data-group="CON_TYPE_FIELD_TYPE"></div></td>';
        html += '<td style=""><input type="checkbox" class="defaultAcc" name="conTypeFieldVO[' + index + '].validSign" lay-skin="switch" checked lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '"></td>';
        html += '<td style=""><input type="text" name="conTypeFieldVO[' + index + '].sortNum" class="sortRow" value="'+(sortRowNum++)+'" disabled/></td>';
        html += '<td><a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a></td>';
        html += '</tr>';
        $("#bankAcc").append(html);
        form.render();
        //数据来源，用到字典
        bt.dicts.init();
    }

    var columnsTab = function() {
        var isCity = !$('#bankAccY').hasClass('hide');
        var columns = [
        {
        	data: "fieldName",
        	render: function (data, row) {
        	    var html='<input type="hidden" name="conTypeFieldVO[' + row + '].id"  value="' + data.id + '" autocomplete="off" class="layui-input" />';
        	    html+='<input style="display: inline;" type="text" name="conTypeFieldVO[' + row + '].fieldName" lay-verify="required"  value="' + data.fieldName + '" autocomplete="off" class="layui-input" />';
        	    html+='<span>&nbsp&nbsp&nbsp'+bt.lang(data.fieldName)+'</span>';
        		return html;
        	}
        },
        {
            data: "rmk",
            render: function (data, row) {
                return '<textarea name="conTypeFieldVO[' + row + '].rmk" class="layui-textarea">'+(data.rmk == null ? '' : data.rmk)+'</textarea>';
            }
        },
        {
            data: "requiredSign",
            render: function (data, row) {
                return '<input type="checkbox" class="defaultAcc" name="conTypeFieldVO[' + row + '].requiredSign" ' + (data.requiredSign == 1 ? 'checked' : '') + ' lay-skin="switch" lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '">';
            }
        },
        {
            data: 'fieldType',
            render: function (data, row) {
                return '<div class="bt-biz-dict" data-defaultvalue="'+data.fieldType+'"  data-required="true" data-type="select" id="fieldType" name="conTypeFieldVO[' + row + '].fieldType" data-name="conTypeFieldVO[' + row + '].fieldType" data-group="CON_TYPE_FIELD_TYPE"></div>';
            }
        },
        {
            data: 'validSign',
            render: function (data, row) {
                return '<input type="checkbox" class="defaultAcc" name="conTypeFieldVO[' + row + '].validSign" ' + (data.validSign == 1 ? 'checked' : '') + ' lay-skin="switch" lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '">';
            }
        },
        {
            data: 'sortNum',
            render: function (data, row) {
                return '<input type="text" class="sortRow" name="conTypeFieldVO[' + row + '].sortNum" value="'+(row+1)+'" disabled/>';
            }
        },
        {
            data: null,
            render: function (data, row) {
                return '<a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a>';
            }
        }
        ];
        return columns;
    }
    
    var extenrnalId = $('input[name="id"]').val();

    //初始化表格
    bankAccTable = bt.bttable.init({
        scrollX: true,
        scrollY: "400px",
        elem: "#bankAcc",
        url: basePath + "/contract/contractType/conFiled.json?id=" + extenrnalId,
        buttons: [
            {
                text: '<i class="fa fa-plus" aria-hidden="true"></i>' + bt.lang('common.addTr'),
                action: function ($button) {
                    var index = rowTr;
                    addRow(index);
                    rowTr ++;
                }
            }
        ],
        columns: columnsTab(),
        delRowCallback: function (data, $tr) {
            $tr.remove();
        },
        tableInitCallback: function (data) {
        	rowTr = data.length;
    	    sortRowNum =$("input[class='sortRow']").length;
            sortRowNum++;
        	bt.dicts.init();
            form.render();
        }
    });
   

    $(document).on('click', ".delete-bankAcc", function () {
        var tr = $(this).parents('tr');
        var index = tr.attr("row");
        
//        $('input[name="externalAcc[' + index + '].externalAcc"]').attr("value","");
        tr.remove();
        
        sortRowNum =$("input[class='sortRow']").length;
        sortRowNum++;
        $("input[class='sortRow']").each(function(index){
        	$(this).val(index+1);
    	});
    });

    function initCorpAndisCommon(isCommon) {
	   	 if (isCommon == "all"){
	      	 var currentPower =$("#currPower").val()
	      	 if (currentPower == "all" || currentPower == "common"){
	           	  $("#inputCorp").find('.select-tree-input').attr("disabled","disabled");
	              $("input[name='shareSign']").prop("checked",true);
	              $(".celRow").hide();
	           }else{
	              $("input[name='shareSign']").prop("checked",false);
	              $("#inputCorp").find('.select-tree-input').attr("lay-verify","required");
	              $(".celRow").show();
	           }
	      }else if (isCommon == "corp"){
	  	      $("#inputCorp").find('.select-tree-input').attr("lay-verify","required");
	          $("input[name='shareSign']").prop("checked",false);
	      	  $("input[name='shareSign']").attr("disabled","disabled");
	      	  $(".celRow").show();
	      }else if (isCommon == "common"){
	      	  $("#inputCorp").find('.select-tree-input').attr("disabled","disabled");
	      	  $("input[name='shareSign']").attr("disabled","disabled");
	          $("input[name='shareSign']").prop("checked",true);
	          $(".celRow").hide();
	      }
        form.renderIcon();
    }

    

});


