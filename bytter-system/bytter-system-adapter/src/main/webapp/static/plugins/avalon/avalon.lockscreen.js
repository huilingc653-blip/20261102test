'use strict';
var lockScreen;
avalon.component('lockScreen', {
    template: heredoc(function () {
        /*
         <div class="lock-screen-container" :if="@show">
         <form onsubmit="return lockScreen.checkSubmit();">
             <div class="lock-screen">
                 <div class="lock"></div>
                 <div class="user" ms-text="@username"></div>
                 <div class="input-content">
                     <input type="password" :duplex="@password" ms-attr="@obj" class="lock-srk">
                     <button type="button" class="unlock-btn" :click="@unlockFunc">{{button}}</button>
                 </div>
             </div>
         </form>
         </div>
         */
    }),
    defaults:{
        show:false,
        password:"",
        username:"",
        element:"",
        obj:{
            placeholder:bt.lang('sys.login.pleaseEnterPassword')
        },
        button:bt.lang('sys.user.button.unlock'),
        onInit:function(e){
            lockScreen = e.vmodel
        },
        onReady:function(e){
            var that = this;
            if(that.element){
                var elem = $("."+this.element);
                elem.click(function(){
                    layer.confirm(bt.lang('sys.home.widget.whetherToLockScreen'), {icon: 3}, function(index){
                        that.lockScreen();
                    });
                });
            }
            that.checkLockScreen();
        },
        checkSubmit:function(daa){
            this.unlockFunc();
            return false;
        },
        checkLockScreen:function(){
            var that = this;
            $.get(basePath+'/sys/user/checkLockScreen',function(result){
                if(result.success){that.lockScreen();}
            },'JSON');
        },
        lockScreen:function(){
            var that = this;
            $.get(basePath+'/sys/user/lockScreen',function(result){
                if(result.success){that.show = true;}
                $(".lock-screen-container .input-content input").focus();
                screenTimerStart(false);
            },'JSON');
        },
        unlockFunc : function(){
            var that = this;
            if(that.password.length == 0){
                layer.alert(bt.lang('sys.login.pleaseEnterPassword'), {icon: 2,zIndex:99999999});
                return;
            }
            $.post(basePath+'/sys/user/unLockScreen',{password:that.password},function(result){
                if(result.success){
                    that.show = false;
                    that.password = "";
                    screenTimerStart(true);
                }else{
                    layer.alert(bt.lang('sys.login.message.password.error'), {icon: 2,zIndex:99999999});
                }
            },'JSON');
        }
    }
});

