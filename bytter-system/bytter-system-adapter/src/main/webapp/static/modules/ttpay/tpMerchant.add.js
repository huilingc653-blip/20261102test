var dicts,corp,$,form,upload;
bt.use(['form','corp','dicts','upload'], function(){
    $ = bt.jquery,form = bt.form();
    upload=layui.upload;
    dicts = bt.dicts.init();
    corp = bt.corp.init();
	//监听保存
	form.on('submit(save)', function(data){
		bt.loading.show();
	    var action="/ttpay/merchant/saveOrUpdate";
	    $.post(basePath + action, $('#inputForm').serialize(), function (result) {
			bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                	location.reload();//重载当前页面
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
	    return false;
	});
}); 

var sonMerchantHtml='<tr>';
sonMerchantHtml+='<td class="textRight" width="15%">'+bt.lang('ttpay.sonMerchantNo')+'</td>';
sonMerchantHtml+='<td width="35%"><div class="layui-input-inline"><input type="text" name="sonMerchantNo" class="layui-input" lay-verify="required|strlength" data-maxlength="200"  placeholder="'+bt.lang('ttpay.sonMerchantNo')+'" value="" ></div></td>';
sonMerchantHtml+='<td class="textRight" width="15%">'+bt.lang('ttpay.rmk')+'</td>';
sonMerchantHtml+='<td><div class="layui-input-inline"><input type="text" name="sonRmk" class="layui-input" lay-verify="strlength" data-maxlength="200"  placeholder="'+bt.lang('ttpay.rmk')+'" value="" ></div></td>';
sonMerchantHtml+='<td><button class="layui-btn" type="button" onclick="deleteTr(this)" >'+bt.lang('common.button.delete')+'</button></td>';
sonMerchantHtml+='</tr>';

var varMerchantHtml='<tr>';
varMerchantHtml+='<td class="textRight" width="10%">'+bt.lang('ttpay.varMerchant.code')+'</td>';
varMerchantHtml+='<td width="15%"><div class="layui-input-inline"><input type="text" name="varCode" class="layui-input" lay-verify="required|strlength" data-maxlength="50"  placeholder="'+bt.lang('ttpay.varMerchant.code')+'" value="" ></div></td>';
varMerchantHtml+='<td class="textRight" width="10%">'+bt.lang('ttpay.varMerchant.name')+'</td>';
varMerchantHtml+='<td width="15%"><div class="layui-input-inline"><input type="text" name="varName" class="layui-input" lay-verify="required|strlength" data-maxlength="50"  placeholder="'+bt.lang('ttpay.varMerchant.name')+'" value="" ></div></td>';
varMerchantHtml+='<td class="textRight" width="10%">'+bt.lang('ttpay.varMerchant.value')+'</td>';
varMerchantHtml+='<td><div class="layui-input-inline"><input type="text" name="varValue" class="layui-input" lay-verify="required|strlength"  placeholder="'+bt.lang('ttpay.varMerchant.value')+'" value="" ><input type="hidden" name="fileId" value="" ></div></td>';
varMerchantHtml+='<td><button class="layui-btn" type="button" onclick="deleteTr(this)" >'+bt.lang('common.button.delete')+'</button></td>';
varMerchantHtml+='</tr>';

var fieldSize=0;
function getVarMerchantHtmlFile(){
	var varMerchantHtmlFile='<tr>';
	varMerchantHtmlFile+='<td class="textRight" width="10%">'+bt.lang('ttpay.varMerchant.code')+'</td>';
	varMerchantHtmlFile+='<td width="15%"><div class="layui-input-inline"><input type="text" name="varCode" class="layui-input" lay-verify="required|strlength" data-maxlength="50"  placeholder="'+bt.lang('ttpay.varMerchant.code')+'" value="" ></div></td>';
	varMerchantHtmlFile+='<td class="textRight" width="10%">'+bt.lang('ttpay.varMerchant.name')+'</td>';
	varMerchantHtmlFile+='<td width="15%"><div class="layui-input-inline"><input type="text" name="varName" class="layui-input" lay-verify="required|strlength" data-maxlength="50"  placeholder="'+bt.lang('ttpay.varMerchant.name')+'" value="" ></div></td>';
	varMerchantHtmlFile+='<td class="textRight" width="10%">'+bt.lang('ttpay.varMerchant.value')+'</td>';
	varMerchantHtmlFile+='<td><div class="layui-input-inline"><input type="hidden" name="varValue" value="" ><input type="hidden" name="fileId" id="upload-'+fieldSize+'-FileId" value="" lay-verify="required">';
	varMerchantHtmlFile+='<input id="upload-'+fieldSize+'" name="upload-'+fieldSize+'" type="file" /><a href="javascript:;"><i class="fa fa-download" title="" id="upload-'+fieldSize+'-I" style="display:none;" onClick="downloadFile(\'#upload-'+fieldSize+'-FileId\')" ></i></a></div></td>';
	varMerchantHtmlFile+='<td><button class="layui-btn" type="button" onclick="deleteTr(this)" >'+bt.lang('common.button.delete')+'</button></td>';
	varMerchantHtmlFile+='</tr>';
	return varMerchantHtmlFile;
}

function addSonMerchant(){
	$("#sonMerchantTable tbody").append(sonMerchantHtml);
	form.renderIcon();
}
function addVarMerchant(){
	$("#varMerchantTable tbody").append(varMerchantHtml);
	form.renderIcon();
}
function addVarMerchantFile(){
	fieldSize++;
	$("#varMerchantTable tbody").append(getVarMerchantHtmlFile());
	form.renderIcon();
	uploadInit('#upload-' + fieldSize);
}
function uploadInit(id){
	layui.upload({
		elem : id,
		url : basePath + '/upload/attachment?code=ttpay',
		type: 'file',
		success : function(res, input) {
			var obj=this;
			if (typeof res.success == 'boolean' && !res.success) {
				layer.alert(bt.lang(res.msg));
			} else if(res[0]) {
				$(obj.elem+"-FileId").val(res[0].id);
				$(obj.elem+"-I").show();
				$(obj.elem+"-I").attr("title",res[0].name+"."+res[0].type);
			}
		}
	}); 
}
function downloadFile(fileId){
	if($(fileId).val()){
		window.location = basePath+"/attachment/sysattachment/download?id="+$(fileId).val();
	}
}

function deleteTr(obj){
	if ($(obj)) {
		$(obj).parents("tr").remove();
	}
}
