/**
 * Created by zhouj on 2017/8/15.
 * 0.0.1 增中一个在顶层页上添加一个upload的属性
 */
function load_js(src, callback) {
    var srcArray = src.split("?")[0].split("/");
    var scr_src = srcArray[srcArray.length - 1];
    // 判断要 添加的脚本是否存在如果存在则不继续添加了
    var scripts = document.getElementsByTagName("script");
    if (!!scripts && 0 != scripts.length) {
        for (var i = 0; i < scripts.length; i++) {
            if (-1 != scripts[i].src.indexOf(scr_src)) {
                callback();
                return true;
            }
        }
    }
    // 不存在需要的则添加
    var head = document.getElementsByTagName("head")[0];
    var script = document.createElement("script");
    script.setAttribute("type", "text/javascript");
    script.setAttribute("src", src);
    script.setAttribute("async", true);
    script.setAttribute("defer", true);
    head.appendChild(script);
    //fuck ie! duck type
    if (document.all) {
        script.onreadystatechange = function() {
            var state = this.readyState;
            if (state === 'loaded' || state === 'complete') {
                callback();
            }
        };
    } else {
        //firefox, chrome
        script.onload = function() {
            callback();
        };
    }
}



//组件封装
var uploadext = layui.define(["jquery","element","layer"], function(exports) {
    jQuery = layui.jquery;
    $ = layui.jquery,layui_element = layui.element();

    //fileMd5;//文件唯一标识
    /******************下面的参数是自定义的*************************/
    var fileName,//文件名称
        oldJindu,//如果该文件之前上传过 已经上传的进度是多少
        count = 0,//当前正在上传的文件在数组中的下标，一次上传多个文件时使用
        filesArr = [],//文件数组：每当有文件被添加进队列的时候 就push到数组中
        map = {};//key存储文件id，value存储该文件上传过的进度
    var btUpload = function(){
        this.paramObj={
            $elem: undefined,
            _config: undefined,
            uploader : undefined,
            filePickerId : this.newGUID(),
            uploaderList : undefined,
            uploaderBtn : undefined,
            uploaderData: [],
            state : 'pending',
            callBackFun: undefined,
            callBackLimitFun: undefined,
            fileLimitNum: undefined,
            hasUploadNum: 0
        };
        this.options = {
            elem: undefined,
            gid: undefined,//组别id
            auto: true,
            pick: {
                id: '#'+this.paramObj.filePickerId,
                label: bt.lang("common.button.selectfile")
            },
            compress: false,//图片不进行压缩
            swf: basePath + '/static/plugins/upload/js/Uploader.swf',
            server: basePath + '/attachment/sysattachment/webUploader',
            resize: false,
            formData: {},
            accept: {title:'*',extensions:'*',mimeTypes:'*/*'},
            chunked: false,//分片处理
            chunkSize: 20 * 1024 * 1024,//每片20M
            chunkRetry: 2,//如果失败，则不重试
            threads: 1,//上传并发数。允许同时最大上传进程数。
            totalNum : 0,//文件总数
            uploadNum : 0,//已经上传文件数
            fileSingleSizeLimit: undefined,
            hasDownload: true,//是否支持下载
            hasDelete: true,//是否支持删除
            hasUpload: true,//是否显示上传按钮
            isExpand: false,//是否展开
            //fileNumLimit: 1,
            success: undefined
        };
        return this;
    };
    btUpload.prototype.newGUID = function(){
        function _sub() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }
        return (_sub() + _sub() + "-" + _sub() + "-" + _sub() + "-" + _sub() + "-" + _sub() + _sub() + _sub());
    };
    btUpload.prototype.getData = function(){
        var $this = this;
        var data = [];
        if($this.paramObj.uploaderData){
            for(var i=0;i<$this.paramObj.uploaderData.length;i++){
                data.push($this.paramObj.uploaderData[0].data);
            }
        }
        return data;
    };
    btUpload.prototype.success = function (callBack) {
        this.paramObj.callBackFun = callBack;
    };
    btUpload.prototype.getGroupId = function(){
        return (this.paramObj.uploaderData && this.paramObj.uploaderData.length>0 && this.paramObj.uploaderData[0].data.groupId)||"";
    };
    btUpload.prototype.getFileData = function () {
        return this.paramObj.uploaderData;
    };
    btUpload.prototype.setFileData = function (data) {
        if(data){
            var obj = {id:data.groupId+"f_list_"+this.paramObj.uploaderData.length, data: data};
            this.paramObj.uploaderData.push(obj);
            this.filehtml(obj);
        }
    };
    btUpload.prototype.filehtml = function (data) {
        if(data){
            var file = data.data;
            this.paramObj.$elem = $(this.options.elem);
            var downhtml = "",delhtml = "",isDealhtml = (this.options.hasDownload||this.options.hasDelete)?1:0;
            if(isDealhtml){
                downhtml = this.options.hasDownload?'<a href="javascript:;"><i class="fa fa-download download-file" title="'+bt.lang("common.message.downfile")+'"></i></a>':'';
                delhtml = this.options.hasDelete?'<a href="javascript:;"><i class="fa fa-trash-o remove-file" title="'+bt.lang("common.message.deletefile")+'"></i></a>':'';
            }
            var dhtml = downhtml+delhtml;
            var fileItem = '<tr class="file-item" id="'+data.id+'">'+
                '<td><div class="pull-left"><i style="display: none;" class="fa fa-file-image-o file-item-ext-'+file.suffix+'"></i>'+file.name+'</div></td>'+
                '<td>'+file.type+'</td>'+
                '<td>'+(file.fileSize/1024).toFixed(2)+'kb</td>'+
                (this.paramObj._config.hasUpload?'<td class="progress" style="width:100px;"></td>':'')+
                '<td><span class="state pull-right">'+bt.lang("common.message.uploaded")+'</span></td>'+
                ''+(isDealhtml?'<td style="text-align: center;"><div>'+dhtml+'</div></td>':'')+''+
                '</tr>';
            this.paramObj.$elem.find(".list-content").append(fileItem);
            $( '#'+data.id ).data("file",file);//赋值
        }
    };
    //获取列表
    btUpload.prototype.getListData = function(data,uploader){
        var $this = this;
        if(!data.gid){
            $this.paramObj.callBackLimitFun = function(data){
                $this.initUpload(data);
            };
            $this.getLimitNum();
            return false;
        }
        $.ajax({
            type:"POST",
            dataType:"json",
            url: basePath + '/attachment/sysattachment/getFileList',//ajax将所有片段合并成整体
            data:{id : data.gid },
            success:function(data){
                if(data && data.length>0){
                    $.extend(true, $this.options, {totalNum:data.length,uploadNum:data.length});
                    $this.paramObj.hasUploadNum = data.length;
                    for(var i=0;i<data.length;i++){
                        $this.setFileData(data[i]);
                    }
                    $($this.options.elem).find(".totalFileNum").text($this.options.totalNum);
                    $($this.options.elem).find(".uploadFileNum").text($this.options.uploadNum);
                }
                $this.paramObj.callBackLimitFun = function(data,list){
                    $this.initUpload(data,list);
                };
                $this.getLimitNum(data);
            },
            error:function () {
                $this.paramObj.callBackLimitFun = function(data){
                    $this.initUpload(data);
                };
                $this.getLimitNum();
            }
        });
    };
    //获取列表
    btUpload.prototype.getLimitNum = function(havedList){
        var $this = this;
        $.ajax({
            type:"POST",
            dataType:"json",
            url: basePath + '/attachment/sysattachment/getLimitNum',//ajax将所有片段合并成整体
            data:{type:"sys",name:"attachmentSize"},
            success:function(data){
                if(data && typeof data.value !=="undefined"){
                    $this.paramObj.fileLimitNum = Number(data.value);
                    $this.paramObj.callBackLimitFun && $this.paramObj.callBackLimitFun(Number(data.value),havedList);
                }else{
                    $this.paramObj.fileLimitNum = undefined;
                    $this.paramObj.callBackLimitFun && $this.paramObj.callBackLimitFun(undefined,havedList);
                }
            },
            error: function(){
                $this.paramObj.fileLimitNum = undefined;
                $this.paramObj.callBackLimitFun && $this.paramObj.callBackLimitFun(undefined,havedList);
            }
        });
    };
    btUpload.prototype.getLimitType = function(type,name,callback){
        $.ajax({
            type:"POST",
            dataType:"json",
            url: basePath + '/attachment/sysattachment/getLimitNum',
            data:{type:type,name:name},
            success:function(data){
                callback && callback(data);
            },
            error: function(){
                callback && callback(0);
            }
        });
    };
    //删除文件
    btUpload.prototype.removeFileEvent = function(){
        var $this = this;
        $this.paramObj.$elem.on('click','.remove-file',function(){
            var $that = $(this);
            var id = $that.parents(".file-item").attr("id");
            if(id){
                var data = $( '#'+id ).data("file");
                if(data && data.id){
                    $.ajax({
                        type:"POST",
                        dataType:"json",
                        url: basePath + '/attachment/sysattachment/deleteFile',//ajax将所有片段合并成整体
                        data:{id : data.id },
                        success:function(data){
                            if(data && data.success){
                                if(id && id.indexOf("f_list")<0){
                                    $this.paramObj.uploader.removeFile(id, true);
                                }
                                $this.paramObj.hasUploadNum--;
                                $that.parents(".file-item").remove();
                                $this.options.totalNum-=1;
                                $this.paramObj.$elem.find(".totalFileNum").text($this.options.totalNum);
                                $this.options.uploadNum-=1;
                                $this.paramObj.$elem.find(".uploadFileNum").text($this.options.uploadNum);

                                for(var i=0;i<$this.paramObj.uploaderData.length;i++){
                                    if(id===$this.paramObj.uploaderData[i].id){
                                        $this.paramObj.uploaderData.splice(i,1);
                                        break;
                                    }
                                }
                                $this.paramObj.uploader.options.success && $this.paramObj.uploader.options.success($this.paramObj.uploaderData);
                                $this.paramObj.callBackFun && $this.paramObj.callBackFun($this.paramObj.uploaderData);
                            }
                        }
                    });
                }else{
                    $this.paramObj.uploader.removeFile(id, true);
                    $that.parents(".file-item").remove();
                    $this.options.totalNum-=1;
                    $this.paramObj.$elem.find(".totalFileNum").text($this.options.totalNum);
                    $this.paramObj.hasUploadNum--;
                }
            }
            return false;
        });
    };

    btUpload.prototype.init = function(options,isReset){
        var that = this;
        if (!isReset){ //非重置时，把系统参数备份一份
            that.duplicationOptions = $.extend(true, {}, that.options);
            that.duplicationParamObj = $.extend(true, {}, that.paramObj);
        }else {
            that.paramObj = $.extend(true, {}, that.duplicationParamObj);
            that.options = $.extend(true, {}, that.duplicationOptions);
        }
        $.extend(true, that.options, options);
        that.paramObj._config = that.options;
        // liuyj 2018-04-28 根据此标识提示关闭页面时，已经上传了
        var select = top?$(top.document): $(document);
        select.find('.layui-tab-title li.layui-this').attr('upload','false');//开启上传标识，先为false,上传了就true;

        var elem = that.paramObj._config.elem;that.paramObj.$elem = $(elem);
        if (elem === undefined) {
            new Error('uploader Error:请配置参数elem,此参数为jQuery选择器!');
        }
        //创建html
        if (isReset){
            that.paramObj.$elem.html("");
        }
        that.paramObj.$elem.append('<input id="jindutiao_'+that.paramObj.filePickerId+'" type="hidden"/>');
        if(that.paramObj._config.hasUpload){
            that.paramObj.$elem.append('<div id="'+that.paramObj.filePickerId+'" style="float: left;margin-right: 10px"></div>');
        }
        if(!that.paramObj._config.auto && that.paramObj._config.hasUpload){
            that.paramObj.uploaderBtn = $('<button type="button" class="layui-btn btn-deal-upload-file" style="float:left;">'+bt.lang("common.button.startupload")+'</button>');
            that.paramObj.$elem.append(that.paramObj.uploaderBtn);
            var $con = $('<div class="btns layui-clear" ></div>').append(that.paramObj.$elem.html());
            that.paramObj.$elem.html($con);
        }
        var dealhtml = (that.paramObj._config.hasDownload||that.paramObj._config.hasDelete)?"<th>"+bt.lang("common.table.thead.operation")+"</th>":"";
        that.paramObj.uploaderList = '<div style="padding-top:5px;"></div><div class="layui-collapse"><div class="layui-colla-item">'+
            '<h3 class="layui-colla-title" style="height: 20px;line-height: 20px;font-size:12px;">'+bt.lang("common.table.filenums")+'<span class="totalFileNum" >0</span>，'+bt.lang("common.table.uploadnums")+'<span class="uploadFileNum">0</span></h3>'+
            '<div class="layui-colla-content '+(that.paramObj._config.isExpand?'layui-show':'')+'" style="padding:0 0;border-top:0px solid #e2e2e2;">'+
            '<div id="fileList" class="uploader-list">'+
            '<table class="layui-table" style="margin: 0 0;border:0px solid #e2e2e2 !important;">'+
            '<thead>'+
            '<tr>'+
            '<th>'+bt.lang("common.table.filename")+'</th>'+
            '<th>'+bt.lang("common.table.filetype")+'</th>'+
            '<th>'+bt.lang("common.table.filesize")+'</th>'+
            (that.paramObj._config.hasUpload?'<th>'+bt.lang("common.table.progress")+'</th>':'')+
            '<th>'+bt.lang("common.table.status")+'</th>'+dealhtml+''+
            '</tr>'+
            '</thead>'+
            '<tbody class="list-content"></tbody>'+
            '</table>'+
            '</div>'+
            '</div>'+
            '</div>'+
            '</div></div>';
        //$elem.append(uploaderList);
        that.paramObj.$elem.html(that.paramObj.$elem.html()+that.paramObj.uploaderList);
        layui_element.init();
        if(that.paramObj._config.gid){
            that.paramObj._config.formData['pid'] = that.paramObj._config.gid;
        }
        //初始化
        //that.initUpload();
        //2019年5月31日16:38:26 新增大小格式限制 start
        var initMaxSizeFn = function (that,callback) {
            that.getLimitType("sys","attachmentMaxSize",function (data) {
                if(data && typeof data.value !=="undefined"){
                    var maxSize = undefined;//
                    try {maxSize = Number(data.value)*1024000}catch (e){maxSize = undefined;}
                    that.paramObj._config.fileSingleSizeLimit = maxSize;
                }
                callback && callback();
            });
        };
        var initFormatFn = function (that,callback) {
            that.getLimitType("sys","attachmentFormat",function (data) {
                if(data && typeof data.value !=="undefined" && data.value){
                    that.paramObj._config.accept.extensions = data.value;
                }
                callback && callback();
            });
        };
        initMaxSizeFn(that,function () {
            initFormatFn(that,function () {
                if(that.paramObj._config.gid){
                    that.getListData(that.paramObj._config,that.paramObj.uploader);
                }else{
                    that.paramObj.callBackLimitFun = function(data){
                        that.initUpload(data);
                    };
                    that.getLimitNum();
                }
            })
        });
        //2019年5月31日16:38:26 新增大小格式限制 end
        //初始化数据
        /*if(that.paramObj._config.gid){
            that.getListData(that.paramObj._config,that.paramObj.uploader);
        }else{
            that.paramObj.callBackLimitFun = function(data){
                that.initUpload(data);
            };
            that.getLimitNum();
        }*/
        //手动提交上传
        that.paramObj.uploaderBtn && that.paramObj.$elem.find('.btn-deal-upload-file').off('click').on( 'click', function() {
            if(that.paramObj.state === 'uploading' ){
                that.paramObj.uploader.stop();
            }else{
                that.paramObj.uploader.upload();
            }
            return false;
        });
        if(!isReset){
            //删除文件
            that.removeFileEvent();
            //下载文件
            that.paramObj.$elem.on('click',".download-file",function(){
                var id = $(this).parents(".file-item").attr("id");
                if(id){
                    var data = $( '#'+id ).data("file");
                    if(data && data.id){
                        window.location = basePath+"/attachment/sysattachment/download?id="+data.id;
                    }else{
                        layui.layer.msg("请先上传后再下载");
                    }
                }
                return false;
            });
        }
        return that.paramObj.uploader;
    };
    btUpload.prototype.initUpload = function(num,files) {
        var $this = this;
        $this.paramObj.uploader && $this.paramObj.uploader.destroy();
        $this.paramObj.uploader = WebUploader.create($this.paramObj._config);
        //当有文件被添加进队列的时候
        $this.paramObj.uploader.on('fileQueued', function( file ) {
            if($this.paramObj.hasUploadNum>=$this.paramObj.fileLimitNum){
                $this.paramObj.uploader.removeFile(file.id, true);
                //uploader.refresh();
                layer.msg("文件数量不能超过"+$this.paramObj.fileLimitNum);
                return false;
            }
            $this.paramObj.hasUploadNum++;
            var downhtml = "",delhtml = "",isDealhtml = ($this.paramObj._config.hasDownload||$this.paramObj._config.hasDelete)?1:0;
            if(isDealhtml){
                downhtml = this.options.hasDownload?'<a href="javascript:;"><i class="fa fa-download download-file" title="'+bt.lang("common.message.downfile")+'"></i></a>':'';
                delhtml = this.options.hasDelete?'<a href="javascript:;"><i class="fa fa-trash-o remove-file" title="'+bt.lang("common.message.deletefile")+'"></i></a>':'';
            }
            var dhtml = downhtml+delhtml;
            var fileItem = '<tr class="file-item" id="'+file.id+'">'+
                '<td><div class="pull-left"><i style="display: none;" class="fa fa-file-image-o file-item-ext-'+file.ext+'"></i>'+file.name+'</div></td>'+
                '<td>'+file.ext+'</td>'+
                '<td>'+(file.size/1024).toFixed(2)+'kb</td>'+
                ($this.paramObj._config.hasUpload?'<td class="progress" style="width:100px;"></td>':'')+
                '<td><span class="state pull-right">'+bt.lang("common.message.waitingupload")+'</span></td>'+
                ''+(isDealhtml?'<td style="text-align: center;"><div>'+dhtml+'</div></td>':'')+''+
                '</tr>';
            $this.paramObj.$elem.find(".list-content").append(fileItem);
            $this.options.totalNum+=1;
            $this.paramObj.$elem.find(".totalFileNum").text($this.options.totalNum);
        });
        //文件上传过程中创建进度条实时显示。
        $this.paramObj.uploader.on( 'uploadProgress', function( file, percentage ) {
            if(percentage===1){
                percentage=0.99
            }
            var $li = $($this.options.elem).find($( '#'+file.id +" .progress")),$percent = $li.find('.layui-progress .layui-progress-bar');
            //避免重复创建
            if (!$percent.length ) {
                $('<div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="file-progress-bar-'+file.id+'">' +
                    '<div class="layui-progress-bar"><span class="layui-progress-text">0%</span></div>' +
                    '</div>').appendTo( $li ).find('.layui-progress-bar');
            }
            $li.find('span.state').text(bt.lang("common.message.upload"));
            layui_element.init();
            layui_element.progress('file-progress-bar-'+file.id, percentage * 100 + '%');
        });
        //上传成功
        $this.paramObj.uploader.on( 'uploadSuccess', function(file, data ) {
            //request.getAttribute("exc");
            if(data.uploadExc != undefined && data.uploadExc != null && data.uploadExc != ""){
                $($this.options.elem).find($( '#'+file.id )).find('span.state').text(bt.lang("common.message.uploaderror"));
                layui_element.progress('file-progress-bar-'+file.id, 0 * 100 + '%');
                $($this.options.elem).find('#'+file.id).data("uploadError", true);//标记该文件上传失败
                layer.msg(data.uploadExc);
                return false;
            }
            $($this.options.elem).find('#'+file.id).data("uploadError", false);//清除失败标记，防止重试成功后被误判
            layui_element.progress('file-progress-bar-'+file.id, 100 + '%');
            $($this.options.elem).find($( '#'+file.id )).find('span.state').text(bt.lang("common.message.uploaded"));
            $($this.options.elem).find($( '#'+file.id )).data("file",data[0]);//赋值
            var obj = {id: file.id, data: data[0]};
            $this.paramObj.uploaderData.push(obj);
            if(!this.options.gid){
                this.options.formData.pid = data[0].groupId;
                this.options.gid = data[0].groupId;
                // btUpload_isUpload = true;
                var select = top?$(top.document): $(document);
                select.find('.layui-tab-title li.layui-this').attr('upload','true');// 代表已经上传了
            }else{
                this.options.formData.pid = this.options.gid;
            }
        });
        //上传失败
        $this.paramObj.uploader.on( 'uploadError', function(file ) {
            $($this.options.elem).find($( '#'+file.id )).find('span.state').text(bt.lang("common.message.uploaderror"));
            $($this.options.elem).find('#'+file.id).data("uploadError", true);//标记该文件上传失败
        });
        $this.paramObj.uploader.on("error", function (type) {
            if (type == "Q_TYPE_DENIED") {
                var fomart = this.option("accept");
                layer.msg(fomart!=="undefined"?"请上传"+fomart[0].extensions+"后缀格式的文件":"请上传正确格式的文件");
            } else if (type == "F_EXCEED_SIZE") {
                var maxSize = this.option("fileSingleSizeLimit");
                layer.msg(maxSize !=="undefined"?"文件大小不能超过"+(maxSize/1024000)+"Mb":"文件大小超过限制");
            }else if(type == "Q_EXCEED_NUM_LIMIT"){
                layer.msg(typeof $this.paramObj.fileLimitNum !=="undefined"?("文件数量不能超过"+$this.paramObj.fileLimitNum):"超出上传文件数量限制");
            }
        });
        //上传完成
        $this.paramObj.uploader.on( 'uploadComplete', function(file, data ) {
            //上传失败的文件不计入已上传数
            if($($this.options.elem).find('#'+file.id).data("uploadError")){
                return;
            }
            $this.options.uploadNum+=1;
            $this.paramObj.$elem.find(".uploadFileNum").text($this.options.uploadNum);
        });
        $this.paramObj.uploader.on( 'all', function( type ) {
            if ( type === 'startUpload' ) {
                $this.paramObj.state = 'uploading';
            } else if ( type === 'stopUpload' ) {
                $this.paramObj.state = 'paused';
            } else if ( type === 'uploadFinished' ) {
                $this.paramObj.state = 'done';
                $this.options.success && $this.options.success($this.paramObj.uploaderData);
                $this.paramObj.callBackFun && $this.paramObj.callBackFun($this.paramObj.uploaderData);
            }
            if($this.paramObj.uploaderBtn){
                if ($this.paramObj.state === 'uploading' ) {
                    $this.paramObj.uploaderBtn.text(bt.lang("common.message.pauseupload"));
                } else {
                    $this.paramObj.uploaderBtn.text(bt.lang("common.button.startupload"));
                }
            }
        });
        /*$this.getLimitType("sys","attachmentMaxSize",function (data) {
            if(data && typeof data.value !=="undefined"){
                var maxSize = undefined;//*1024000
                try {maxSize = Number(data.value)}catch (e){maxSize = undefined;}
                $this.paramObj.uploader.option("fileSingleSizeLimit",maxSize);
                $this.paramObj.uploader.refresh();
            }
        });
        $this.getLimitType("sys","attachmentFormat",function (data) {
            if(data && typeof data.value !=="undefined" && data.value){
                that.paramObj._config.accept.extensions = data.value;
            }
        });*/
    };

    btUpload.prototype.redraw = function(ops){
        if(ops.gid && ops.gid=="false"){
            ops.gid = "";
        }
        this.paramObj.uploader && this.paramObj.uploader.destroy();
        this.init(ops, true)
    }

    load_js(basePath+'/static/plugins/upload/js/webuploader.js',function(){
        var obj = {
            init:function(ops){
                if(ops.gid && ops.gid=="false"){
                    ops.gid = "";
                }
                var cUploader = new btUpload();
                cUploader.init(ops);
                return cUploader;
            },
            /*,
            getData: function(){
                return this.cUploader.getData();
            },
            getGroupId: function(){
                return this.cUploader.getGroupId();
            },
            success: function(callBack){
                this.cUploader.success(callBack);
            }*/
        };
        exports("btUpload",obj)
    });
});
uploadext.addcss('../../upload/css/webuploader.css', 'webuploader');
uploadext.addcss('../../upload/css/uploader.css', 'uploader');
