/**
 * Created by zhouj on 2017/11/17.
 */
document.addEventListener( "plusready", onPlusReady, false );
function onPlusReady() {
    document.addEventListener( "pause", onAppPause, false );
    document.addEventListener( "resume", onAppReume, false );
}
function onAppPause() {
    alert( "Application paused!" );
}
function onAppReume() {
    alert( "Application resumed!" );
}