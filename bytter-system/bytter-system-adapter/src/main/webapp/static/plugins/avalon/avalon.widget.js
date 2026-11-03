'use strict';
if(!bt.widget){bt.widget = [];}
avalon.component('widgetContent', {
    template: heredoc(function () {
        /*
         <div :css="{height:@column.height?@column.height:200}"></div>
         */
    }),
    defaults: {
        column:{},
        widgetRefreshWatch:{},
        contentWatch:{},
        resizeFlag:true,
        refreshComplete:function(){},
        onInit:function(e){
            if(bt.widgetContainer){
                var model = e.vmodel;
                bt.widgetContainer[model.$id]=model;
            }
        },
        onReady:function(e){
            var that = this;
            that.getContent(e);
            that.widgetRefreshWatch = e.vmodel.$watch('column.widgetName', function(a, b){
                if(bt.widget[b]){
                    that.resizeFlag = false;
                    bt.widget[b].onDispose();
                }
            });
            that.contentWatch = e.vmodel.$watch('column.content', function(a, b){
                that.getContent();
            });
            $(e.target).resize(function(){
                var model = that.column.$model;
                try{
                    if(that.resizeFlag && bt.widget[model.widgetName] && bt.widget[model.widgetName].onResize){
                        bt.widget[model.widgetName].onResize();
                    }
                }catch (e){}
            });
        },
        onDispose:function(){
            //组件销毁时，注销监听
            this.widgetRefreshWatch();
            this.contentWatch();
        },
        getContent:function(e){
            var that = this;
            var model = that.column.$model;
            if(bt.widget[model.widgetName] && bt.widget[model.widgetName].onDispose && avalon.isFunction(bt.widget[model.widgetName].onDispose)){
                bt.widget[model.widgetName].onDispose();
            }
            $.ajax({
                type:"post",
                url:basePath + "/sys/home/widgetContent",
                dataType: "html",
                contentType: "application/json",
                data:JSON.stringify(model),
                success:function(result) {
                    $(that.$element).html(result);
                    that.resizeFlag = true;
                    setTimeout(function(){
                        if(bt.widget[model.widgetName]){
                            bt.widget[model.widgetName].onReady(that);
                        }
                    },50);
                    if(avalon.isFunction(that.refreshComplete)){
                        setTimeout(function(){
                            that.refreshComplete();
                        },500);
                    }
                }
            });
        },
        refreshFunc:function(refreshComplete){
            var that = this;
            that.refreshComplete = refreshComplete;
            that.getContent();
        }
    }
});