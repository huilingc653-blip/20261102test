'use strict';
function heredoc(fn) {
    return fn.toString().replace(/^[^\/]+\/\*!?\s?/, '').
    replace(/\*\/[^\/]+$/, '').trim().replace(/>\s*</g, '><')
}
avalon.component('ms-modal', {
    template: heredoc(function () {
        /*
         <div ms-visible="@config.isShow">
             <div class="layui-layer-shade" :css="{zIndex:@zIndex}" style="background-color: rgb(0, 0, 0); opacity: 0.3;"></div>
             <div class="layui-layer layui-layer-page layer-anim" :css="{width:@width,height:@height,zIndex:@zIndex}">
                 <div class="layui-layer-title" style="cursor: move;">{{@config.title}}</div>
                 <div class="layui-layer-content">
                    <slot name="content"></slot>
                 </div>
                 <span class="layui-layer-setwin">
                    <a class="layui-layer-min" href="javascript:;"><cite></cite></a>
                    <a class="layui-layer-ico layui-layer-max" href="javascript:;"></a>
                    <a class="layui-layer-ico layui-layer-close layui-layer-close1" href="javascript:;"  :click="@close"></a>
                 </span>
                 <span class="layui-layer-resize"></span>
             </div>
         </div>
         */
    }),
    defaults: {
        title: '信息',
        move:true,
        resize:true,
        moveOut:false,
        config: {},
        dict:{
            moveStart:false,
            resizeStart:false,
            offset:[]
        },
        width:850,
        height:530,
        zIndex: 19891014,
        buttonEvent: function (ok) {
            var that = this;
            var cbName = ok ? 'success': 'cancel';
            if (this.config.hasOwnProperty(cbName)) {
                var ret = this.config[cbName](that);
                // if (ret !== false || (ret && typeof ret.next === 'function')) {
                //     this.config.isShow = false
                // }
            } else {
                this.close();
            }
        },
        close:function(){
            this.config.isShow = false
        },
        style : function(layero, options, limit){
            var contElem = layero.find('.layui-layer-content')
                ,type = layero.attr('type')
                ,titHeight = layero.find('.layui-layer-title').outerHeight() || 0
                ,btnHeight = layero.find('.layui-layer-btn').outerHeight() || 0
                ,minLeft = layero.attr('minLeft');

            if(!limit){
                if(parseFloat(options.width) <= 260){
                    options.width = 260;
                }
                if(parseFloat(options.height) - titHeight - btnHeight <= 64){
                    options.height = 64 + titHeight + btnHeight;
                }
            }
            layero.css(options);
            btnHeight = layero.find('.layui-layer-title').outerHeight();
            contElem.css({
                height: parseFloat(options.height) - titHeight - btnHeight - parseFloat(contElem.css('padding-top')) - parseFloat(contElem.css('padding-bottom'))
            })
        },
        min : function(layero){
            var titHeight = layero.find('.layui-layer-title').outerHeight() || 0
                ,left = 0
                ,position = layero.css('position');
            var win = $(window);
            this.record(layero);
            layero.attr('position', position);

            this.style(layero, {
                width: 180
                ,height: titHeight
                ,left: left
                ,top: win.height() - titHeight
                ,position: 'fixed'
                ,overflow: 'hidden'
            }, true);

            layero.find('.layui-layer-min').hide();
            layero.attr('minLeft', left);
        },
        restore : function(layero){
            var area = layero.attr('area').split(',');
            var type = layero.attr('type');
            this.style(layero, {
                width: parseFloat(area[0]),
                height: parseFloat(area[1]),
                top: parseFloat(area[2]),
                left: parseFloat(area[3]),
                position: layero.attr('position'),
                overflow: 'visible'
            }, true);
            layero.find('.layui-layer-max').removeClass('layui-layer-maxmin');
            layero.find('.layui-layer-min').show();
        },
        record : function(layero){
            var area = [
                layero.width(),
                layero.height(),
                layero.position().top,
                layero.position().left + parseFloat(layero.css('margin-left'))
            ];
            layero.find('.layui-layer-max').addClass('layui-layer-maxmin');
            layero.attr({area: area});
        },
        full : function(layero){
            var win = $(window);
            var timer,that=this;
            that.record(layero);
            clearTimeout(timer);
            timer = setTimeout(function(){
                var isfix = layero.css('position') === 'fixed';
                that.style(layero, {
                    top: isfix ? 0 : win.scrollTop(),
                    left: isfix ? 0 : win.scrollLeft(),
                    width: win.width(),
                    height: win.height()
                }, true);
                layero.find('.layui-layer-min').hide();
            }, 100);
        },
        onReady: function(){
            var that = this;
            var el = that.$element;
            var win = $(window);
            var layerBox = $(el);
            var layero = layerBox.find(".layui-layer");
            var moveElem = layero.find('.layui-layer-title');
            var resizeElem = layero.find('.layui-layer-resize');

            this.$watch('config.isShow', function(a){
                if(a){
                    var area = [layero.outerWidth(), layero.outerHeight()];
                    layero.css({left: (win.width() - area[0])/2,top: (win.height() - area[1])/2});
                }
            });

            moveElem.on('mousedown', function(e){
                e.preventDefault();
                if(that.move){
                    that.dict.moveStart = true;
                    that.dict.offset = [
                        e.clientX - parseFloat(layero.css('left'))
                        ,e.clientY - parseFloat(layero.css('top'))
                    ];
                }
            });

            resizeElem.on('mousedown', function(e){
                e.preventDefault();
                that.dict.resizeStart = true;
                that.dict.offset = [e.clientX, e.clientY];
                that.dict.area = [layero.outerWidth(),layero.outerHeight()];
            });

            avalon.bind(window, 'mousemove',function(e){
                if(that.dict.moveStart && that.config.isShow){
                    e.preventDefault();
                    var X = e.clientX - that.dict.offset[0],Y = e.clientY - that.dict.offset[1],fixed = layero.css('position') === 'fixed';

                    that.dict.stX = fixed ? 0 : win.scrollLeft();
                    that.dict.stY = fixed ? 0 : win.scrollTop();

                    //控制元素不被拖出窗口外
                    if(!that.moveOut){
                        var setRig = win.width() - layero.outerWidth() + that.dict.stX,setBot = win.height() - layero.outerHeight() + that.dict.stY;
                        X < that.dict.stX && (X = that.dict.stX);
                        X > setRig && (X = setRig);
                        Y < that.dict.stY && (Y = that.dict.stY);
                        Y > setBot && (Y = setBot);
                    }
                    layero.css({left: X,top: Y});
                }

                //Resize
                if(that.resize && that.dict.resizeStart){
                    e.preventDefault();
                    var X = e.clientX - that.dict.offset[0],Y = e.clientY - that.dict.offset[1];

                    that.style(layero, {width: that.dict.area[0] + X,height: that.dict.area[1] + Y});
                }
            });

            avalon.bind(window,'mouseup', function(e){
                if(that.dict.moveStart){
                    that.dict.moveStart = false;
                }
                if(that.dict.resizeStart){
                    that.dict.resizeStart = false;
                }
            });

            //最小化
            layero.find('.layui-layer-min').on('click', function(){
                that.min(layero);
            });

            layero.find('.layui-layer-max').on('click', function(){
                if($(this).hasClass('layui-layer-maxmin')){
                    that.restore(layero);
                } else {
                    that.full(layero);
                }
            });
        }
    },
    soleSlot: 'content'
});