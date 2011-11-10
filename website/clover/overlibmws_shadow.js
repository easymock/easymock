/*
 overlibmws_shadow.js plug-in module - Copyright Foteos Macrides 2003-2008. All rights reserved.
   For support of the SHADOW feature.
   Initial: July 14, 2003 - Last Revised: January 26, 2008
 See the Change History and Command Reference for overlibmws via:

	http://www.macridesweb.com/oltest/

 Published under an open source license: http://www.macridesweb.com/oltest/license.html
*/

OLloaded=0;
var OLshadowCmds='shadow,shadowx,shadowy,shadowcolor,shadowimage,shadowopacity';
OLregCmds(OLshadowCmds);

// DEFAULT CONFIGURATION
if(OLud('shadow'))var ol_shadow=0;
if(OLud('shadowx'))var ol_shadowx=5;
if(OLud('shadowy'))var ol_shadowy=5;
if(OLud('shadowcolor'))var ol_shadowcolor="#666666";
if(OLud('shadowimage'))var ol_shadowimage="";
if(OLud('shadowopacity'))var ol_shadowopacity=60;
// END CONFIGURATION

var o3_shadow=0,o3_shadowx=5,o3_shadowy=5,o3_shadowcolor="#666666",o3_shadowimage="";
var o3_shadowopacity=60,bkdrop=null;

function OLloadShadow(){
OLload(OLshadowCmds);
}

function OLparseShadow(pf,i,ar){
var k=i,p=OLpar,q=OLparQuo;
if(k<ar.length){
if(Math.abs(ar[k])==SHADOW){OLtoggle(ar[k],pf+'shadow');return k;}
if(ar[k]==SHADOWX){p(ar[++k],pf+'shadowx');return k;}
if(ar[k]==SHADOWY){p(ar[++k],pf+'shadowy');return k;}
if(ar[k]==SHADOWCOLOR){q(ar[++k],pf+'shadowcolor');return k;}
if(ar[k]==SHADOWIMAGE){q(ar[++k],pf+'shadowimage');return k;}
if(ar[k]==SHADOWOPACITY){p(ar[++k],pf+'shadowopacity');return k;}}
return -1;
}

function OLdispShadow(){
if(o3_shadow){OLgetShadowLyrRef();if(bkdrop)OLgenerateShadowLyr();}
}

function OLinitShadow(){
if(OLie55&&OLfilterPI&&o3_filter){if(o3_shadow){o3_shadow=0;
if(!o3_filtershadow){o3_filtershadow=2;o3_filtershadowcolor=o3_shadowcolor;}}return;}
var o;if(!(o=OLmkLyr((OLovertwoPI&&over2&&over==over2?'backdrop2':'backdrop'),
o3_frame,999))||bkdrop!=o){bkdrop=null;OLgetShadowLyrRef();}
}

function OLgetShadowLyrRef(){
if(bkdrop||!o3_shadow)return;
bkdrop=OLgetRefById((OLovertwoPI&&over2&&over==over2?'backdrop2':'backdrop'));
if(!bkdrop){o3_shadow=0;bkdrop=null;}
}

function OLgenerateShadowLyr(){
var wd=(OLns4?over.clip.width:over.offsetWidth),hgt=(OLns4?over.clip.height:over.offsetHeight);
if(OLns4){bkdrop.clip.width=wd;bkdrop.clip.height=hgt;
if(o3_shadowimage)bkdrop.background.src=o3_shadowimage;
else{bkdrop.bgColor=o3_shadowcolor;bkdrop.zIndex=over.zIndex -1;}
}else{var o=bkdrop.style;o.width=wd+'px';o.height=hgt+'px';
if(o3_shadowimage)o.backgroundImage="url("+o3_shadowimage+")";
else o.backgroundColor=o3_shadowcolor;
o.clip='rect(0px '+wd+'px '+hgt+'px 0px)';o.zIndex=over.style.zIndex -1;
if(o3_shadowopacity){var op=o3_shadowopacity;op=(op<=100&&op>0?op:100);
if(OLie4&&!OLieM&&typeof o.filter=='string'){
o.filter='Alpha(opacity='+op+')';if(OLie55&&typeof bkdrop.filters=='object')
bkdrop.filters.alpha.enabled=1;}else{op=op/100;OLopBk(op);}}}
}

function OLopBk(op){
var o=bkdrop.style;
if(typeof o.opacity!='undefined')o.opacity=op;
else if(typeof o.MozOpacity!='undefined')o.MozOpacity=op;
else if(typeof o.KhtmlOpacity!='undefined')o.KhtmlOpacity=op;
}

function OLcleanUpShadow(){
if(!bkdrop)return;
if(OLns4){bkdrop.bgColor=null;bkdrop.background.src=null;}else{
var o=bkdrop.style;o.backgroundColor='transparent';o.backgroundImage='none';
if(OLie4&&!OLieM&&typeof o.filter=='string'){
o.filter='Alpha(opacity=100)';if(OLie55&&typeof bkdrop.filters=='object')
bkdrop.filters.alpha.enabled=0;}else OLopBk(1.0);
if(OLns6){o.width=1+'px';o.height=1+'px';
OLrepositionTo(bkdrop,o3_frame.pageXOffset,o3_frame.pageYOffset);}}
}

function OLshowShadow(){if(bkdrop&&o3_shadow){var o=(OLns4?bkdrop:bkdrop.style);
if(!OLns4&&!OLieM&&(OLfilterPI&&o3_filter&&o3_fadein))OLopOvSh(1);o.visibility="visible";}
}

function OLhideShadow(){
if(bkdrop&&o3_shadow){var o=OLgetRefById((OLovertwoPI&&over2&&over==over2?
'backdrop2':'backdrop'));if(o&&o==bkdrop){var os=(OLns4?bkdrop:bkdrop.style);
if(OLns4||OLieM||!OLfilterPI||((OLfilterPI)&&(!o3_filter||!o3_fadeout||!OLhasOp()))){
os.visibility="hidden";OLcleanUpShadow();}}}
}

function OLrepositionShadow(X,Y){
if(bkdrop&&o3_shadow)OLrepositionTo(bkdrop,X+o3_shadowx,Y+o3_shadowy);
}

OLregRunTimeFunc(OLloadShadow);
OLregCmdLineFunc(OLparseShadow);

OLshadowPI=1;
OLloaded=1;
