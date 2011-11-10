/*
 Do not remove or change this notice.
 overlibmws.js core module - Copyright Foteos Macrides 2002-2008. All rights reserved.
   Initial: August 18, 2002 - Last Revised: March 22, 2008
 This module is subject to the same terms of usage as for Erik Bosrup's overLIB,
 though only a minority of the code and API now correspond with Erik's version.
 See the overlibmws Change History and Command Reference via:

	http://www.macridesweb.com/oltest/

 Published under an open source license: http://www.macridesweb.com/oltest/license.html
 Give credit on sites that use overlibmws and submit changes so others can use them as well.
 You can get Erik's version via: http://www.bosrup.com/web/overlib/
*/

// PRE-INIT -- Ignore these lines, configuration is below.
var OLloaded=0,OLbubblePI=0,OLcrossframePI=0,OLdebugPI=0,OLdraggablePI=0,OLexclusivePI=0,OLfilterPI=0,
OLfunctionPI=0,OLhidePI=0,OLiframePI=0,OLmodalPI=0,OLovertwoPI=0,OLscrollPI=0,OLshadowPI=0,OLprintPI=0,
pmCnt=1,pMtr=new Array(),OLcmdLine=new Array(),OLrunTime=new Array(),OLv,OLudf,OLrefXY,
OLpct=new Array("83%","67%","83%","100%","117%","150%","200%","267%");if(typeof OLgateOK=='undefined')var OLgateOK=1;
var OLp1or2c='inarray,caparray,caption,closetext,right,left,center,autostatuscap,padx,pady,below,above,vcenter,donothing',
OLp1or2co='nofollow,background,offsetx,offsety,fgcolor,bgcolor,cgcolor,textcolor,capcolor,width,wrap,wrapmax,height,border,'
+'base,status,autostatus,snapx,snapy,fixx,fixy,relx,rely,midx,midy,ref,refc,refp,refx,refy,fgbackground,bgbackground,'
+'cgbackground,fullhtml,capicon,textfont,captionfont,textsize,captionsize,timeout,delay,hauto,vauto,nojustx,nojusty,fgclass,'
+'bgclass,cgclass,capbelow,textpadding,textfontclass,captionpadding,captionfontclass,sticky,noclose,mouseoff,offdelay,'
+'closecolor,closefont,closesize,closeclick,closetitle,closefontclass,decode',OLp1or2o='text,cap,close,hpos,vpos,padxl,'
+'padxr,padyt,padyb',OLp1co='label',OLp1or2=OLp1or2co+','+OLp1or2o,OLp1=OLp1co+','+'frame';
OLregCmds(OLp1or2c+','+OLp1or2co+','+OLp1co);
function OLud(v){return eval('typeof ol_'+v+'=="undefined"')?1:0;}

// DEFAULT CONFIGURATION -- See overlibConfig.txt for descriptions
if(OLud('fgcolor'))var ol_fgcolor="#ccccff";
if(OLud('bgcolor'))var ol_bgcolor="#333399";
if(OLud('cgcolor'))var ol_cgcolor="#333399";
if(OLud('textcolor'))var ol_textcolor="#000000";
if(OLud('capcolor'))var ol_capcolor="#ffffff";
if(OLud('closecolor'))var ol_closecolor="#eeeeff";
if(OLud('textfont'))var ol_textfont="Verdana,Arial,Helvetica";
if(OLud('captionfont'))var ol_captionfont="Verdana,Arial,Helvetica";
if(OLud('closefont'))var ol_closefont="Verdana,Arial,Helvetica";
if(OLud('textsize'))var ol_textsize=1;
if(OLud('captionsize'))var ol_captionsize=1;
if(OLud('closesize'))var ol_closesize=1;
if(OLud('fgclass'))var ol_fgclass="";
if(OLud('bgclass'))var ol_bgclass="";
if(OLud('cgclass'))var ol_cgclass="";
if(OLud('textpadding'))var ol_textpadding=2;
if(OLud('textfontclass'))var ol_textfontclass="";
if(OLud('captionpadding'))var ol_captionpadding=2;
if(OLud('captionfontclass'))var ol_captionfontclass="";
if(OLud('closefontclass'))var ol_closefontclass="";
if(OLud('close'))var ol_close="Close";
if(OLud('closeclick'))var ol_closeclick=0;
if(OLud('closetitle'))var ol_closetitle="Click to Close";
if(OLud('text'))var ol_text="Default Text";
if(OLud('cap'))var ol_cap="";
if(OLud('capbelow'))var ol_capbelow=0;
if(OLud('background'))var ol_background="";
if(OLud('width'))var ol_width=200;
if(OLud('wrap'))var ol_wrap=0;
if(OLud('wrapmax'))var ol_wrapmax=0;
if(OLud('height'))var ol_height= -1;
if(OLud('border'))var ol_border=1;
if(OLud('base'))var ol_base=0;
if(OLud('offsetx'))var ol_offsetx=10;
if(OLud('offsety'))var ol_offsety=10;
if(OLud('sticky'))var ol_sticky=0;
if(OLud('nofollow'))var ol_nofollow=0;
if(OLud('noclose'))var ol_noclose=0;
if(OLud('mouseoff'))var ol_mouseoff=0;
if(OLud('offdelay'))var ol_offdelay=300;
if(OLud('hpos'))var ol_hpos=RIGHT;
if(OLud('vpos'))var ol_vpos=BELOW;
if(OLud('status'))var ol_status="";
if(OLud('autostatus'))var ol_autostatus=0;
if(OLud('snapx'))var ol_snapx=0;
if(OLud('snapy'))var ol_snapy=0;
if(OLud('fixx'))var ol_fixx= -1;
if(OLud('fixy'))var ol_fixy= -1;
if(OLud('relx'))var ol_relx=null;
if(OLud('rely'))var ol_rely=null;
if(OLud('midx'))var ol_midx=null;
if(OLud('midy'))var ol_midy=null;
if(OLud('ref'))var ol_ref="";
if(OLud('refc'))var ol_refc='UL';
if(OLud('refp'))var ol_refp='UL';
if(OLud('refx'))var ol_refx=0;
if(OLud('refy'))var ol_refy=0;
if(OLud('fgbackground'))var ol_fgbackground="";
if(OLud('bgbackground'))var ol_bgbackground="";
if(OLud('cgbackground'))var ol_cgbackground="";
if(OLud('padxl'))var ol_padxl=1;
if(OLud('padxr'))var ol_padxr=1;
if(OLud('padyt'))var ol_padyt=1;
if(OLud('padyb'))var ol_padyb=1;
if(OLud('fullhtml'))var ol_fullhtml=0;
if(OLud('capicon'))var ol_capicon="";
if(OLud('frame'))var ol_frame=self;
if(OLud('timeout'))var ol_timeout=0;
if(OLud('delay'))var ol_delay=0;
if(OLud('hauto'))var ol_hauto=0;
if(OLud('vauto'))var ol_vauto=0;
if(OLud('nojustx'))var ol_nojustx=0;
if(OLud('nojusty'))var ol_nojusty=0;
if(OLud('label'))var ol_label="";
if(OLud('decode'))var ol_decode=0;
// ARRAY CONFIGURATION - See overlibConfig.txt for descriptions.
if(OLud('texts'))var ol_texts=new Array("Text 0","Text 1");
if(OLud('caps'))var ol_caps=new Array("Caption 0","Caption 1");
// END CONFIGURATION -- Don't change anything below, all configuration is above.

// INIT -- Runtime variables.
var o3_text="",o3_cap="",o3_sticky=0,o3_nofollow=0,o3_background="",o3_noclose=0,o3_mouseoff=0,o3_offdelay=300,o3_hpos=RIGHT,
o3_offsetx=10,o3_offsety=10,o3_fgcolor="",o3_bgcolor="",o3_cgcolor="",o3_textcolor="",o3_capcolor="",o3_closecolor="",
o3_width=200,o3_wrap=0,o3_wrapmax=0,o3_height= -1,o3_border=1,o3_base=0,o3_status="",o3_autostatus=0,o3_snapx=0,o3_snapy=0,
o3_fixx= -1,o3_fixy= -1,o3_relx=null,o3_rely=null,o3_midx=null,o3_midy=null,o3_ref="",o3_refc='UL',o3_refp='UL',o3_refx=0,
o3_refy=0,o3_fgbackground="",o3_bgbackground="",o3_cgbackground="",o3_padxl=0,o3_padxr=0,o3_padyt=0,o3_padyb=0,o3_fullhtml=0,
o3_vpos=BELOW,o3_capicon="",o3_textfont="Verdana,Arial,Helvetica",o3_captionfont="",o3_closefont="",o3_textsize=1,OLcC=null,
o3_captionsize=1,o3_closesize=1,o3_frame=self,o3_timeout=0,o3_delay=0,o3_hauto=0,o3_vauto=0,o3_nojustx=0,o3_nojusty=0,
o3_close="",o3_closeclick=0,o3_closetitle="",o3_fgclass="",o3_bgclass="",o3_cgclass="",o3_textpadding=2,o3_textfontclass="",
o3_captionpadding=2,o3_captionfontclass="",o3_closefontclass="",o3_capbelow=0,o3_label="",o3_decode=0,
CSSOFF=DONOTHING,CSSCLASS=DONOTHING,over=null,OLdelayid=0,OLtimerid=0,OLshowid=0,OLndt=0,OLfnRef="",OLhover=0,OLx=0,OLy=0,
OLshowingsticky=0,OLallowmove=0,OLoverHTML="",OLover2HTML="",OLifRef="",OLo2Ref="",OLifX=0,OLifY=0,
OLua=(OLv=navigator.userAgent)?OLv.toLowerCase():'',
OLns4=(navigator.appName=='Netscape'&&parseInt(navigator.appVersion)==4)?1:0,
OLns6=(document.getElementById)?1:0,
OLie4=(document.all)?1:0,
OLgek=(OLv=OLua.match(/gecko\/(\d{8})/i))?parseInt(OLv[1]):0,
OLmac=(OLua.indexOf('mac')>=0)?1:0,
OLsaf=(OLua.indexOf('safari')>=0)?1:0,
OLkon=(OLua.indexOf('konqueror')>=0)?1:0,
OLkht=(OLsaf||OLkon)?1:0,
OLopr=(OLua.indexOf('opera')>=0)?1:0,
OLop7=(OLopr&&document.createTextNode)?1:0;
if(OLopr){OLns4=OLns6=OLgek=0;OLie4=(OLop7)?1:0;}
var OLieM=((OLie4&&OLmac)&&!(OLkht||OLopr))?1:0,
OLie5=0,OLie55=0;OLie7=0;if(OLie4&&!OLop7){
if((OLv=OLua.match(/msie (\d\.\d+)\.*/i))&&(OLv=parseFloat(OLv[1]))>=5.0){
OLie5=1;OLns6=0;if(OLv>=5.5)OLie55=1;if(OLv>=7.0)OLie7=1;}if(OLns6)OLie4=0;}
if(OLns4)window.onresize=function(){location.reload();};var OLchkMh=1,OLdw;
if(OLns4||OLie4||OLns6){OLmh();if(window.addEventListener)window.addEventListener("unload",
OLulCl,false);}else{overlib=nd=cClick=OLpageDefaults=no_overlib;}
function OLulCl(){if(over)cClick();window.removeEventListener("unload",OLulCl,false);}

/*
 PUBLIC FUNCTIONS
*/
// Loads defaults then args into runtime variables.
function overlib(){
if(!(OLloaded&&OLgateOK))return;if((OLexclusivePI)&&OLisExclusive(arguments))return true;if(OLchkMh)OLmh();
if(OLndt&&!OLtimerid)OLndt=0;if(over)cClick();if(parent!=self){if(parent.OLo2Ref){parent.OLeval(parent.OLo2Ref);
parent.OLo2Ref="";}if(parent.OLifRef){parent.OLeval(parent.OLifRef);parent.OLifRef="";}}if(OLo2Ref){eval(OLo2Ref);
OLo2Ref="";}if(OLifRef){eval(OLifRef);OLifRef="";}OLload(OLp1or2);OLload(OLp1);OLfnRef="";OLifX=0;OLifY=0;OLhover=0;
OLsetRunTimeVar();OLparseTokens('o3_',arguments);if(!(over=OLmkLyr()))return false;if(o3_decode)OLdecode();if(OLprintPI)
OLchkPrint();if(OLbubblePI)OLchkForBubbleEffect();if(OLdebugPI)OLsetDebugCanShow();if(OLshadowPI)OLinitShadow();
if(OLiframePI)OLinitIfs();if(OLfilterPI)OLinitFilterLyr();if(OLexclusivePI&&o3_exclusive&&o3_exclusivestatus!="")
o3_status=o3_exclusivestatus;else if(o3_autostatus==2&&o3_cap!="")o3_status=o3_cap;else if(o3_autostatus==1&&o3_text!="")
o3_status=o3_text;if(!o3_delay){return OLmain();}else{OLdelayid=setTimeout("OLmain()",o3_delay);if(o3_status!=""){
self.status=o3_status;return true;}else if(!(OLop7&&event&&event.type=='mouseover'))return false;}
}
function OLeval(s){eval(s);}

// Clears popups if appropriate
function nd(time){
if(OLloaded&&OLgateOK){if(!((OLexclusivePI)&&OLisExclusive())){if(time&&over&&!o3_delay){
if(OLtimerid>0)clearTimeout(OLtimerid);OLtimerid=(OLhover&&o3_frame==self&&!OLcursorOff())?0:
setTimeout("cClick()",(o3_timeout=OLndt=time));}else{if(!OLshowingsticky){OLallowmove=0;
if(over)OLhideObject(over);}}}}return false;
}

// Close function for stickies
function cClick(){
if(OLloaded&&OLgateOK){OLhover=0;if(over){if(OLo2Ref){eval(OLo2Ref);OLo2Ref="";}if(OLovertwoPI&&over==over2)cClick2();
OLhideObject(over);OLshowingsticky=0;OLallowmove=0;}if(OLmodalPI)OLclearModal();}return false;
}

// Sets page-specific defaults.
function OLpageDefaults(){
OLparseTokens('ol_',arguments);
}

// Gets object referenced by its id or name
function OLgetRef(l,d){var r=OLgetRefById(l,d);return (r)?r:OLgetRefByName(l,d);}

// For unsupported browsers.
function no_overlib(){return false;}

/*
 OVERLIB MAIN FUNCTION SET
*/
function OLmain(){
o3_delay=0;if(parent!=self&&o3_frame==parent&&parent.OLscrollPI&&parent.over)parent.OLclearScroll();if(o3_frame==self){
if(o3_noclose)OLoptMOUSEOFF(0);else if(o3_mouseoff)OLoptMOUSEOFF(1);}if(o3_sticky){OLshowingsticky=1;if(OLfnRef&&
parent!=self&&o3_frame==parent&&parent.overlib){parent.OLifRef=OLfnRef+'cClick()';}}OLdoLyr();OLallowmove=0;if(o3_timeout>0){
if(OLtimerid>0)clearTimeout(OLtimerid);OLtimerid=setTimeout("cClick()",o3_timeout);}OLchkRef();OLdisp(o3_status);
if(OLdraggablePI)OLcheckDrag();if(o3_status!="")return true;else if(!(OLop7&&event&&event.type=='mouseover'))return false;
}
function OLchkRef(){
if(o3_ref){OLrefXY=OLgetRefXY(o3_ref);if(OLrefXY[0]==null&&OLcrossframePI)OLchkIfRef();
if(OLrefXY[0]==null){o3_ref="";o3_midx=0;o3_midy=0;}}
}

// Loads o3_ variables
function OLload(c){var i,m=c.split(',');for(i=0;i<m.length;i++)eval('o3_'+m[i]+'=ol_'+m[i]);}

// Chooses LGF 
function OLdoLGF(){
return (o3_background!=''||o3_fullhtml)?OLcontentBackground(o3_text,o3_background,o3_fullhtml):(o3_cap=="")?
OLcontentSimple(o3_text):(o3_sticky)?OLcontentCaption(o3_text,o3_cap,o3_close):OLcontentCaption(o3_text,o3_cap,'');
}

// Makes Layer
function OLmkLyr(id,f,z){
id=(id||'overDiv');f=(f||o3_frame);z=(z||1000);var fd=f.document,d=OLgetRefById(id,fd);
if(!d){if(OLns4)d=fd.layers[id]=new Layer(1024,f);else if(OLie4&&!OLop7){
fd.body.insertAdjacentHTML('AfterBegin','<div id="'+id+'"></div>');d=fd.all[id];}else{d=fd.createElement('div');
if(d){d.id=id;fd.body.appendChild(d);}}if(!d)return null;if(OLns4)d.zIndex=z;else{var o=d.style;o.position='absolute';
o.visibility='hidden';o.zIndex=z;}}return d;
}

// Creates and writes layer content
function OLdoLyr(){
if(o3_sticky&&OLtimerid>0){clearTimeout(OLtimerid);OLtimerid=0;}if(o3_background==''&&!o3_fullhtml){
if(o3_fgbackground!='')o3_fgbackground=' background="'+o3_fgbackground+'"';
if(o3_bgbackground!='')o3_bgbackground=' background="'+o3_bgbackground+'"';
if(o3_cgbackground!='')o3_cgbackground=' background="'+o3_cgbackground+'"';
if(o3_fgcolor!='')o3_fgcolor=' bgcolor="'+o3_fgcolor+'"';if(o3_bgcolor!='')o3_bgcolor=' bgcolor="'+o3_bgcolor+'"';
if(o3_cgcolor!='')o3_cgcolor=' bgcolor="'+o3_cgcolor+'"';if(o3_height>0)o3_height=' height="'+o3_height+'"';
else o3_height='';}if(!OLns4)OLrepositionTo(over,(OLns6?20:0),0);var lyrHtml=OLdoLGF();
if(o3_wrap&&!o3_fullhtml){OLlayerWrite(lyrHtml);o3_width=(OLns4?over.clip.width:over.offsetWidth);if(OLie4){
var w=OLfd().clientWidth;if(o3_width>=w){if(OLop7){if(OLovertwoPI&&over==over2){var z=over2.style.zIndex;
o3_frame.document.body.removeChild(over);over2=OLmkLyr('overDiv2',o3_frame,z);over=over2;}else{
o3_frame.document.body.removeChild(over);over=OLmkLyr();}}o3_width=w-20;}}
if(o3_wrapmax<1&&o3_frame.innerWidth)o3_wrapmax=o3_frame.innerWidth-40;
if(o3_wrapmax>0&&o3_width>o3_wrapmax)o3_width=o3_wrapmax;o3_wrap=0;lyrHtml=OLdoLGF();}OLlayerWrite(lyrHtml);
o3_width=(OLns4?over.clip.width:over.offsetWidth);if(OLbubblePI)OLgenerateBubble(lyrHtml);
}

/*
 LAYER GENERATION FUNCTIONS
*/
// Makes simple table without caption
function OLcontentSimple(txt){
var t=OLbgLGF()+OLfgLGF(txt)+OLbaseLGF();OLsetBackground('');return t;
}

// Makes table with caption and optional close link
function OLcontentCaption(txt,title,close){
var closing=(OLprintPI?OLprintCapLGF():''),closeevent='onmouseover',caption,t,cC='javascript:return '+OLfnRef
+(OLovertwoPI&&over==over2?'cClick2();':'cClick();');if(o3_closeclick)closeevent=(o3_closetitle?'title="'
+o3_closetitle+'" ':'')+'onclick';if(o3_capicon!=''&&o3_capicon.indexOf('<img')!=0)o3_capicon='<img src="'+o3_capicon
+'" /> ';if(close){closing+='<td align="right"><a href="'+cC+'" '+closeevent+'="'+cC+'"'+(o3_closefontclass?' class="'
+o3_closefontclass+'">':(OLns4?'><':'')+OLlgfUtil(0,1,'','a',o3_closecolor,o3_closefont,o3_closesize))+close+
(o3_closefontclass?'':(OLns4?OLlgfUtil(1,1,'','a'):''))+'</a></td>';}caption='<table id="overCap'
+(OLovertwoPI&&over==over2?'2':'')+'"'+OLwd(0)+' border="0" cellpadding="'+o3_captionpadding+'" cellspacing="0"'
+(o3_cgclass?' class="'+o3_cgclass+'"':o3_cgcolor+o3_cgbackground)+'><tr><td'+OLwd(0)+(o3_cgclass?' class="'
+o3_cgclass+'">':'>')+(o3_captionfontclass?'<div'+OLhL(1)+' class="'+o3_captionfontclass+'">':OLlgfUtil(0,1,'','div',
o3_capcolor,o3_captionfont,o3_captionsize))+o3_capicon+title+OLlgfUtil(1,1,'','div')+'</td>'+closing+'</tr></table>';
t=OLbgLGF()+(o3_capbelow?OLfgLGF(txt)+caption:caption+OLfgLGF(txt))+OLbaseLGF();OLsetBackground('');return t;
}

// For BACKGROUND and FULLHTML commands
function OLcontentBackground(txt,image,hasfullhtml){
var t;if(hasfullhtml){t=txt;}else{t='<table'+OLwd(1)+' border="0" cellpadding="0" '+'cellspacing="0" '+'height="'
+o3_height+'"><tr><td colspan="3" height="'+o3_padyt+'"></td></tr><tr><td width="'+o3_padxl+'"></td><td valign="top"'
+OLwd(2)+'>'+OLlgfUtil(0,0,o3_textfontclass,'div',o3_textcolor,o3_textfont,o3_textsize)+txt+OLlgfUtil(1,0,'','div')
+'</td><td width="'+o3_padxr+'"></td></tr><tr><td colspan="3" height="'+o3_padyb+'"></td></tr></table>';}
OLsetBackground(image);return t;
}

// LGF utilities
function OLbgLGF(){
return '<table'+OLwd(1)+o3_height+' border="0" cellpadding="'+o3_border+'" cellspacing="0"'+(o3_bgclass?' class="'
+o3_bgclass+'"':o3_bgcolor+o3_bgbackground)+'><tr><td>';
}
function OLfgLGF(t){
return '<table'+OLwd(0)+o3_height+' border="0" cellpadding="'+o3_textpadding+'" cellspacing="0"'+(o3_fgclass?' class="'
+o3_fgclass+'"':o3_fgcolor+o3_fgbackground)+'><tr><td valign="top"'+(o3_fgclass?' class="'+o3_fgclass+'"':'')+'>'
+OLlgfUtil(0,0,o3_textfontclass,'div',o3_textcolor,o3_textfont,o3_textsize)+t+(OLprintPI?OLprintFgLGF():'')
+OLlgfUtil(1,0,'','div')+'</td></tr></table>';
}
function OLlgfUtil(end,stg,tfc,ele,col,fac,siz){
if(end)return('</'+(OLns4?'font'+(stg?'></strong':''):ele)+'>');else return(tfc?'<div'+OLhL(1)+' class="'+tfc+'">':
((ele=='a'?'':'<')+(OLns4?(stg?'strong><':'')+'font color="'+col+'" face="'+OLquoteMultiNameFonts(fac)+'" size="'
+siz:(ele=='a'?'':ele)+' style="'+((ele=='div')?OLhL(0):'')+'color:'+col+(stg?';font-weight:bold':'')+';font-family:'
+OLquoteMultiNameFonts(fac)+';font-size:'+siz+';'+(ele=='span'?'text-decoration:underline;':''))+'">'));
}
function OLquoteMultiNameFonts(f){
var i,v,pM=f.split(',');for(i=0;i<pM.length;i++){v=pM[i];v=v.replace(/^\s+/,'').replace(/\s+$/,'');
if(/\s/.test(v) && !/['"]/.test(v)){v="\'"+v+"\'";pM[i]=v;}}return pM.join();
}
function OLbaseLGF(){
return ((o3_base>0&&!o3_wrap)?('<table width="100%" border="0" cellpadding="0" cellspacing="0"'+(o3_bgclass?' class="'
+o3_bgclass+'"':'')+'><tr><td height="'+o3_base+'"></td></tr></table>'):'')+'</td></tr></table>';
}
function OLwd(a){return(o3_wrap?'':' width="'+(!a?'100%':(a==1?o3_width:(o3_width-o3_padxl-o3_padxr)))+'"');}
function OLhL(s){return(s?' style="width:100%;"':'width:100%;');}

// Loads image into the div.
function OLsetBackground(i){
if(i==''){if(OLns4)over.background.src=null;else{if(OLns6)over.style.width='';over.style.backgroundImage='none';}}
else{if(OLns4)over.background.src=i;else{if(OLns6)over.style.width=o3_width+'px';over.style.backgroundImage='url('+i+')';}}
}

/*
 HANDLING FUNCTIONS
*/
// Displays layer
function OLdisp(s){
if(OLmodalPI&&!o3_modalscroll)OLchkModal();if(!OLallowmove){if(OLshadowPI)OLdispShadow();if(OLiframePI)OLdispIfs();
OLplaceLayer();if(OLmodalPI&&o3_modalscroll)OLchkModal();if(OLndt)OLshowObject(over);
else OLshowid=setTimeout("OLshowObject(over)",1);OLallowmove=(o3_sticky||o3_nofollow)?0:1;}OLndt=0;if(s!="")self.status=s;
}

// Decides placement of layer.
function OLplaceLayer(){
var snp,X,Y,pgLeft,pgTop,pWd=o3_width,pHt,iWd=100,iHt=100,SB=0,LM=0,CX=0,TM=0,BM=0,CY=0,o=OLfd(),
nsb=(OLgek>=20010505&&!o3_frame.scrollbars.visible)?1:0;
if(!OLkht&&o&&o.clientWidth)iWd=o.clientWidth;
else if(o3_frame.innerWidth){SB=Math.ceil(1.4*(o3_frame.outerWidth-o3_frame.innerWidth));
if(SB>20)SB=20;iWd=o3_frame.innerWidth;}
pgLeft=(OLie4)?o.scrollLeft:o3_frame.pageXOffset;
if(OLie55&&OLfilterPI&&o3_filter&&o3_filtershadow)SB=CX=5;else
if((OLshadowPI)&&bkdrop&&o3_shadow&&o3_shadowx){SB+=((o3_shadowx>0)?o3_shadowx:0);
LM=((o3_shadowx<0)?Math.abs(o3_shadowx):0);CX=Math.abs(o3_shadowx);}
if(o3_ref!=""||o3_fixx> -1||o3_relx!=null||o3_midx!=null){
if(o3_ref!=""){X=OLrefXY[0];if(OLie55&&OLfilterPI&&o3_filter&&o3_filtershadow){
if(o3_refp=='UR'||o3_refp=='LR')X-=5;}
else if((OLshadowPI)&&bkdrop&&o3_shadow&&o3_shadowx){
if(o3_shadowx<0&&(o3_refp=='UL'||o3_refp=='LL'))X-=o3_shadowx;else
if(o3_shadowx>0&&(o3_refp=='UR'||o3_refp=='LR'))X-=o3_shadowx;}
}else{if(o3_midx!=null){
X=parseInt(pgLeft+((iWd-pWd-SB-LM)/2)+o3_midx);
}else{if(o3_relx!=null){
if(o3_relx>=0)X=pgLeft+o3_relx+LM;else X=pgLeft+o3_relx+iWd-pWd-SB;
}else{X=o3_fixx+LM;}}}
}else{
if(o3_hauto){
if(o3_hpos==LEFT&&OLx-pgLeft+OLifX<iWd/2&&OLx-pWd-o3_offsetx+OLifX<pgLeft+LM)o3_hpos=RIGHT;else
if(o3_hpos==RIGHT&&OLx-pgLeft+OLifX>iWd/2&&OLx+pWd+o3_offsetx+OLifX>pgLeft+iWd-SB)o3_hpos=LEFT;}
X=(o3_hpos==CENTER)?parseInt(OLx-((pWd+CX)/2)+o3_offsetx):
(o3_hpos==LEFT)?OLx-o3_offsetx-pWd:OLx+o3_offsetx;
if(o3_snapx>1){
snp=X % o3_snapx;
if(o3_hpos==LEFT){X=X-(o3_snapx+snp);}else{X=X+(o3_snapx-snp);}}X+=OLifX;}
if(!o3_nojustx&&X+pWd>pgLeft+iWd-SB)
X=iWd+pgLeft-pWd-SB;if(!o3_nojustx&&X-LM<pgLeft)X=pgLeft+LM;
pgTop=OLie4?o.scrollTop:o3_frame.pageYOffset;
if(!OLkht&&!nsb&&o&&o.clientHeight)iHt=o.clientHeight;
else if(o3_frame.innerHeight)iHt=o3_frame.innerHeight;
if(OLbubblePI&&o3_bubble)pHt=OLbubbleHt;else pHt=OLns4?over.clip.height:over.offsetHeight;
if((OLshadowPI)&&bkdrop&&o3_shadow&&o3_shadowy){TM=(o3_shadowy<0)?Math.abs(o3_shadowy):0;
if(OLie55&&OLfilterPI&&o3_filter&&o3_filtershadow)BM=CY=5;else
BM=(o3_shadowy>0)?o3_shadowy:0;CY=Math.abs(o3_shadowy);}
if(o3_ref!=""||o3_fixy> -1||o3_rely!=null||o3_midy!=null){
if(o3_ref!=""){Y=OLrefXY[1];if(OLie55&&OLfilterPI&&o3_filter&&o3_filtershadow){
if(o3_refp=='LL'||o3_refp=='LR')Y-=5;}else if((OLshadowPI)&&bkdrop&&o3_shadow&&o3_shadowy){
if(o3_shadowy<0&&(o3_refp=='UL'||o3_refp=='UR'))Y-=o3_shadowy;else
if(o3_shadowy>0&&(o3_refp=='LL'||o3_refp=='LR'))Y-=o3_shadowy;}
}else{if(o3_midy!=null){
Y=parseInt(pgTop+((iHt-pHt-CY)/2)+o3_midy);
}else{if(o3_rely!=null){
if(o3_rely>=0)Y=pgTop+o3_rely+TM;else Y=pgTop+o3_rely+iHt-pHt-BM;}else{
Y=o3_fixy+TM;}}}
}else{
if(o3_vauto){
if(o3_vpos==ABOVE&&OLy-pgTop+OLifY<iHt/2&&OLy-pHt-o3_offsety+OLifY<pgTop)o3_vpos=BELOW;else
if(o3_vpos==BELOW&&OLy-pgTop+OLifY>iHt/2&&OLy+pHt+o3_offsety+((OLns4||OLkht)?17:0)+OLifY>pgTop+iHt-BM)
o3_vpos=ABOVE;}Y=(o3_vpos==VCENTER)?parseInt(OLy-((pHt+CY)/2)+o3_offsety):
(o3_vpos==ABOVE)?OLy-(pHt+o3_offsety+BM):OLy+o3_offsety+TM;
if(o3_snapy>1){
snp=Y % o3_snapy;
if(pHt>0&&o3_vpos==ABOVE){Y=Y-(o3_snapy+snp);}else{Y=Y+(o3_snapy-snp);}}Y+=OLifY;}
if(!o3_nojusty&&Y+pHt+BM>pgTop+iHt)Y=pgTop+iHt-pHt-BM;if(!o3_nojusty&&Y-TM<pgTop)Y=pgTop+TM;
OLrepositionTo(over,X,Y);
if(OLshadowPI)OLrepositionShadow(X,Y);if(OLiframePI)OLrepositionIfs(X,Y);
if(OLns6&&o3_frame.innerHeight){iHt=o3_frame.innerHeight;OLrepositionTo(over,X,Y);}
if(OLscrollPI)OLchkScroll(X-pgLeft,Y-pgTop);
}

// Chooses body or documentElement
function OLfd(f){
var fd=((f)?f:o3_frame).document,fdc=fd.compatMode,fdd=fd.documentElement;
return (!OLop7&&fdc&&fdc!='BackCompat'&&fdd&&fdd.clientWidth)?fd.documentElement:fd.body;
}

// Gets location of REFerence object
function OLgetRefXY(r,d){
var o=OLgetRef(r,d),ob=o,rXY=[o3_refx,o3_refy],of;if(!o)return [null,null];if(OLns4){
if(typeof o.length!='undefined'&&o.length>1){ob=o[0];rXY[0]+=o[0].x+o[1].pageX;rXY[1]+=o[0].y+o[1].pageY;}else{
if((o.toString().indexOf('Image')!= -1)||(o.toString().indexOf('Anchor')!= -1)){rXY[0]+=o.x;rXY[1]+=o.y;}
else{rXY[0]+=o.pageX;rXY[1]+=o.pageY;}}}else{rXY[0]+=OLpageLoc(o,'Left');rXY[1]+=OLpageLoc(o,'Top');}
of=OLgetRefOffsets(ob);rXY[0]+=of[0];rXY[1]+=of[1];return rXY;
}

// Seeks REFerence by id
function OLgetRefById(l,d){
l=(l||'overDiv');d=(d||o3_frame.document);var j,r;if(d.getElementById)return d.getElementById(l);
if(OLie4&&d.all)return d.all[l];if(d.layers&&d.layers.length>0){if(d.layers[l])return d.layers[l];
for(j=0;j<d.layers.length;j++){r=OLgetRefById(l,d.layers[j].document);if(r)return r;}}return null;
}

// Seeks REFerence by name
function OLgetRefByName(l,d){
d=(d||o3_frame.document);var j,r,v=OLie4?d.all.tags('iframe'):OLns6?d.getElementsByTagName('iframe'):null;
if(typeof d.images!='undefined'&&d.images[l])return d.images[l];
if(typeof d.anchors!='undefined'&&d.anchors[l])return d.anchors[l];
if(v)for(j=0;j<v.length;j++)if(v[j].name==l)return v[j];if(d.layers&&d.layers.length>0)for(j=0;j<d.layers.length;j++){
r=OLgetRefByName(l,d.layers[j].document);if(r&&r.length>0)return r;else if(r)return [r,d.layers[j]];}return null;
}

// Gets layer vs REFerence offsets
function OLgetRefOffsets(o){
var c=o3_refc.toUpperCase(),p=o3_refp.toUpperCase(),W=0,H=0,pW=0,pH=0,of=[0,0];pW=(OLbubblePI&&o3_bubble)?
o3_width:OLns4?over.clip.width:over.offsetWidth;pH=(OLbubblePI&&o3_bubble)?OLbubbleHt:OLns4?
over.clip.height:over.offsetHeight;if((!OLop7)&&o.toString().indexOf('Image')!= -1){W=o.width;H=o.height;}
else if((!OLop7)&&o.toString().indexOf('Anchor')!= -1){c=o3_refc='UL';}else{W=(OLns4)?o.clip.width:o.offsetWidth;
H=(OLns4)?o.clip.height:o.offsetHeight;}if((OLns4||(OLns6&&OLgek))&&o.border){W+=2*parseInt(o.border);
H+=2*parseInt(o.border);}if(c=='UL'){of=(p=='UR')?[-pW,0]:(p=='LL')?[0,-pH]:(p=='LR')?[-pW,-pH]:[0,0];}else if(c=='UR'){
of=(p=='UR')?[W-pW,0]:(p=='LL')?[W,-pH]:(p=='LR')?[W-pW,-pH]:[W,0];}else if(c=='LL'){of=(p=='UR')?[-pW,H]:(p=='LL')?[0,H-pH]:
(p=='LR')?[-pW,H-pH]:[0,H];}else if(c=='LR'){of=(p=='UR')?[W-pW,H]:(p=='LL')?[W,H-pH]:(p=='LR')?[W-pW,H-pH]:[W,H];}return of;
}

// Gets x or y location of object
function OLpageLoc(o,t){
var l=0,s=o;while(o.offsetParent&&o.offsetParent.tagName.toLowerCase()!='html'){l+=o['offset'+t];o=o.offsetParent;}
l+=o['offset'+t];while(s=s.parentNode){if((s['scroll'+t]>0)&&s.tagName.toLowerCase()=='div')l-=s['scroll'+t];}return l;
}

// Moves layer
function OLmouseMove(e){
var e=(e||event);OLcC=(OLovertwoPI&&over2&&over==over2?cClick2:cClick);OLx=(e.pageX||e.clientX+OLfd().scrollLeft);
OLy=(e.pageY||e.clientY+OLfd().scrollTop);if((OLallowmove&&over)&&(o3_frame==self||over==OLgetRefById()||(OLovertwoPI&&
over2==over&&over==OLgetRefById('overDiv2')))){OLplaceLayer();if(OLhidePI)OLhideUtil(0,1,1,0,0,0);}if(OLhover&&over&&
o3_frame==self&&OLcursorOff())if(o3_offdelay<1)OLcC();else{if(OLtimerid>0)clearTimeout(OLtimerid);
OLtimerid=setTimeout("OLcC()",o3_offdelay);}
}

// Capture mouse and chain other scripts.
function OLmh(){
var fN,f,j,k,s,mh=OLmouseMove,w=(OLns4&&window.onmousemove),re=/function[ ]*(\w*)\(/;OLdw=document;if(document.onmousemove||
w){if(w)OLdw=window;f=OLdw.onmousemove.toString();fN=f.match(re);if(!fN||fN[1]=='anonymous'||fN[1]=='OLmouseMove'){OLchkMh=0;
return;}if(fN[1])s=fN[1]+'(e)';else{j=f.indexOf('{');k=f.lastIndexOf('}')+1;s=f.substring(j,k);}s+=';OLmouseMove(e);';
mh=new Function('e',s);}OLdw.onmousemove=mh;if(OLns4)OLdw.captureEvents(Event.MOUSEMOVE);
}

/*
 PARSING
*/
function OLparseTokens(pf,ar){
var i,v,md= -1,par=(pf!='ol_'),p=OLpar,q=OLparQuo,t=OLtoggle;OLudf=(par&&!ar.length?1:0);
for(i=0;i<ar.length;i++){if(md<0){if(typeof ar[i]=='number'){OLudf=(par?1:0);i--;}
else{switch(pf){case 'ol_':ol_text=ar[i];break;default:o3_text=ar[i];}}md=0;}else{
if(ar[i]==INARRAY){OLudf=0;eval(pf+'text=ol_texts['+ar[++i]+']');continue;}
if(ar[i]==CAPARRAY){eval(pf+'cap=ol_caps['+ar[++i]+']');continue;}
if(ar[i]==CAPTION){q(ar[++i],pf+'cap');continue;}
if(Math.abs(ar[i])==STICKY){t(ar[i],pf+'sticky');continue;}
if(Math.abs(ar[i])==NOFOLLOW){t(ar[i],pf+'nofollow');continue;}
if(ar[i]==BACKGROUND){q(ar[++i],pf+'background');continue;}
if(Math.abs(ar[i])==NOCLOSE){t(ar[i],pf+'noclose');continue;}
if(Math.abs(ar[i])==MOUSEOFF){t(ar[i],pf+'mouseoff');continue;}
if(ar[i]==OFFDELAY){p(ar[++i],pf+'offdelay');continue;}
if(ar[i]==RIGHT||ar[i]==LEFT||ar[i]==CENTER){p(ar[i],pf+'hpos');continue;}
if(ar[i]==OFFSETX){p(ar[++i],pf+'offsetx');continue;}
if(ar[i]==OFFSETY){p(ar[++i],pf+'offsety');continue;}
if(ar[i]==FGCOLOR){q(ar[++i],pf+'fgcolor');continue;}
if(ar[i]==BGCOLOR){q(ar[++i],pf+'bgcolor');continue;}
if(ar[i]==CGCOLOR){q(ar[++i],pf+'cgcolor');continue;}
if(ar[i]==TEXTCOLOR){q(ar[++i],pf+'textcolor');continue;}
if(ar[i]==CAPCOLOR){q(ar[++i],pf+'capcolor');continue;}
if(ar[i]==CLOSECOLOR){q(ar[++i],pf+'closecolor');continue;}
if(ar[i]==WIDTH){p(ar[++i],pf+'width');continue;}
if(Math.abs(ar[i])==WRAP){t(ar[i],pf+'wrap');continue;}
if(ar[i]==WRAPMAX){p(ar[++i],pf+'wrapmax');continue;}
if(ar[i]==HEIGHT){p(ar[++i],pf+'height');continue;}
if(ar[i]==BORDER){p(ar[++i],pf+'border');continue;}
if(ar[i]==BASE){p(ar[++i],pf+'base');continue;}
if(ar[i]==STATUS){q(ar[++i],pf+'status');continue;}
if(Math.abs(ar[i])==AUTOSTATUS){v=pf+'autostatus';
eval(v+'=('+ar[i]+'<0)?('+v+'==2?2:0):('+v+'==1?0:1)');continue;}
if(Math.abs(ar[i])==AUTOSTATUSCAP){v=pf+'autostatus';
eval(v+'=('+ar[i]+'<0)?('+v+'==1?1:0):('+v+'==2?0:2)');continue;}
if(ar[i]==CLOSETEXT){q(ar[++i],pf+'close');continue;}
if(ar[i]==SNAPX){p(ar[++i],pf+'snapx');continue;}
if(ar[i]==SNAPY){p(ar[++i],pf+'snapy');continue;}
if(ar[i]==FIXX){p(ar[++i],pf+'fixx');continue;}
if(ar[i]==FIXY){p(ar[++i],pf+'fixy');continue;}
if(ar[i]==RELX){p(ar[++i],pf+'relx');continue;}
if(ar[i]==RELY){p(ar[++i],pf+'rely');continue;}
if(ar[i]==MIDX){p(ar[++i],pf+'midx');continue;}
if(ar[i]==MIDY){p(ar[++i],pf+'midy');continue;}
if(ar[i]==REF){q(ar[++i],pf+'ref');continue;}
if(ar[i]==REFC){q(ar[++i],pf+'refc');continue;}
if(ar[i]==REFP){q(ar[++i],pf+'refp');continue;}
if(ar[i]==REFX){p(ar[++i],pf+'refx');continue;}
if(ar[i]==REFY){p(ar[++i],pf+'refy');continue;}
if(ar[i]==FGBACKGROUND){q(ar[++i],pf+'fgbackground');continue;}
if(ar[i]==BGBACKGROUND){q(ar[++i],pf+'bgbackground');continue;}
if(ar[i]==CGBACKGROUND){q(ar[++i],pf+'cgbackground');continue;}
if(ar[i]==PADX){p(ar[++i],pf+'padxl');p(ar[++i],pf+'padxr');continue;}
if(ar[i]==PADY){p(ar[++i],pf+'padyt');p(ar[++i],pf+'padyb');continue;}
if(Math.abs(ar[i])==FULLHTML){t(ar[i],pf+'fullhtml');continue;}
if(ar[i]==BELOW||ar[i]==ABOVE||ar[i]==VCENTER){p(ar[i],pf+'vpos');continue;}
if(ar[i]==CAPICON){q(ar[++i],pf+'capicon');continue;}
if(ar[i]==TEXTFONT){q(ar[++i],pf+'textfont');continue;}
if(ar[i]==CAPTIONFONT){q(ar[++i],pf+'captionfont');continue;}
if(ar[i]==CLOSEFONT){q(ar[++i],pf+'closefont');continue;}
if(ar[i]==TEXTSIZE){q(ar[++i],pf+'textsize');continue;}
if(ar[i]==CAPTIONSIZE){q(ar[++i],pf+'captionsize');continue;}
if(ar[i]==CLOSESIZE){q(ar[++i],pf+'closesize');continue;}
if(ar[i]==TIMEOUT){p(ar[++i],pf+'timeout');continue;}
if(ar[i]==DELAY){p(ar[++i],pf+'delay');continue;}
if(Math.abs(ar[i])==HAUTO){t(ar[i],pf+'hauto');continue;}
if(Math.abs(ar[i])==VAUTO){t(ar[i],pf+'vauto');continue;}
if(Math.abs(ar[i])==NOJUSTX){t(ar[i],pf+'nojustx');continue;}
if(Math.abs(ar[i])==NOJUSTY){t(ar[i],pf+'nojusty');continue;}
if(Math.abs(ar[i])==CLOSECLICK){t(ar[i],pf+'closeclick');continue;}
if(ar[i]==CLOSETITLE){q(ar[++i],pf+'closetitle');continue;}
if(ar[i]==FGCLASS){q(ar[++i],pf+'fgclass');continue;}
if(ar[i]==BGCLASS){q(ar[++i],pf+'bgclass');continue;}
if(ar[i]==CGCLASS){q(ar[++i],pf+'cgclass');continue;}
if(ar[i]==TEXTPADDING){p(ar[++i],pf+'textpadding');continue;}
if(ar[i]==TEXTFONTCLASS){q(ar[++i],pf+'textfontclass');continue;}
if(ar[i]==CAPTIONPADDING){p(ar[++i],pf+'captionpadding');continue;}
if(ar[i]==CAPTIONFONTCLASS){q(ar[++i],pf+'captionfontclass');continue;}
if(ar[i]==CLOSEFONTCLASS){q(ar[++i],pf+'closefontclass');continue;}
if(Math.abs(ar[i])==CAPBELOW){t(ar[i],pf+'capbelow');continue;}
if(ar[i]==LABEL){q(ar[++i],pf+'label');continue;}
if(Math.abs(ar[i])==DECODE){t(ar[i],pf+'decode');continue;}
if(ar[i]==DONOTHING){continue;}
i=OLparseCmdLine(pf,i,ar);}}
if((OLfunctionPI)&&OLudf&&o3_function)o3_text=o3_function();
if(pf=='o3_')OLfontSize();
}
function OLpar(a,v){eval(v+'='+a);}
function OLparQuo(a,v){eval(v+"='"+OLescSglQt(a)+"'");}
function OLescSglQt(s){return s.toString().replace(/\\/g,"\\\\").replace(/'/g,"\\'");}
function OLtoggle(a,v){eval(v+'=('+v+'==0&&'+a+'>=0)?1:0');}
function OLhasDims(s){return /[%\-a-z]+$/.test(s);}
function OLfontSize(){
var i;if(OLhasDims(o3_textsize)){if(OLns4)o3_textsize="2";}else
if(!OLns4){i=parseInt(o3_textsize);o3_textsize=(i>0&&i<8)?OLpct[i]:OLpct[0];}
if(OLhasDims(o3_captionsize)){if(OLns4)o3_captionsize="2";}else
if(!OLns4){i=parseInt(o3_captionsize);o3_captionsize=(i>0&&i<8)?OLpct[i]:OLpct[0];}
if(OLhasDims(o3_closesize)){if(OLns4)o3_closesize="2";}else
if(!OLns4){i=parseInt(o3_closesize);o3_closesize=(i>0&&i<8)?OLpct[i]:OLpct[0];}
if(OLprintPI)OLprintDims();
}
function OLdecode(){
var re=/%[0-9A-Fa-f]{2,}/,t=o3_text,c=o3_cap,u=unescape,d=!OLns4&&(!OLgek||OLgek>=20020826)&&typeof decodeURIComponent?
decodeURIComponent:u;if(typeof(window.TypeError)=='function'){if(re.test(t)){eval(new Array('try{','o3_text=d(t);',
'}catch(e){','o3_text=u(t);','}').join('\n'))};if(c&&re.test(c)){eval(new Array('try{','o3_cap=d(c);','}catch(e){',
'o3_cap=u(c);','}').join('\n'))}}else{if(re.test(t))o3_text=u(t);if(c&&re.test(c))o3_cap=u(c);}
}

/*
 LAYER FUNCTIONS
*/
// Writes to layer
function OLlayerWrite(t){
t+="\n";if(OLns4){over.document.write(t);over.document.close();}else if(typeof over.innerHTML!='undefined'){
if(OLieM)over.innerHTML='';over.innerHTML=t;}else{var range=o3_frame.document.createRange();range.setStartAfter(over);
var domfrag=range.createContextualFragment(t);while(over.hasChildNodes()){over.removeChild(over.lastChild);}
over.appendChild(domfrag);}if(OLovertwoPI&&over==over2)OLover2HTML=t;else OLoverHTML=t;
if(OLprintPI)over.print=o3_print?t:null;
}

// Makes object visible
function OLshowObject(o){
OLshowid=0;o=(OLns4)?o:o.style;if(((OLfilterPI)&&!OLchkFilter(o))||!OLfilterPI)o.visibility="visible";
if(OLshadowPI)OLshowShadow();if(OLiframePI)OLshowIfs();if(OLhidePI)OLhideUtil(1,1,0);
}

// Hides object
function OLhideObject(o){
if(OLshowid>0){clearTimeout(OLshowid);OLshowid=0;}if(OLtimerid>0)clearTimeout(OLtimerid);
if(OLdelayid>0)clearTimeout(OLdelayid);OLtimerid=0;OLdelayid=0;self.status="";o3_label=ol_label;
if(o3_frame!=self)o=OLgetRefById();if(o){if(o.onmouseover)o.onmouseover=null;if(OLscrollPI&&o==over)OLclearScroll();
if(OLdraggablePI)OLclearDrag();if(OLfilterPI)OLcleanupFilter(o);if(OLshadowPI)OLhideShadow();var os=(OLns4)?o:o.style;
if(((OLfilterPI)&&!OLchkFadeOut(os))||!OLfilterPI){os.visibility="hidden";if(!OLie55||!OLfilterPI||!o3_filter||
o3_fadeout<0)o.innerHTML='';}if(OLhidePI&&o==over)OLhideUtil(0,0,1);if(OLiframePI)OLhideIfs(o);}
}

// Moves layer
function OLrepositionTo(o,xL,yL){
o=(OLns4)?o:o.style;o.left=(OLns4?xL:xL+'px');o.top=(OLns4?yL:yL+'px');
}

// Handle NOCLOSE-MOUSEOFF
function OLoptMOUSEOFF(c){
if(!c)o3_close="";
over.onmouseover=function(){OLhover=1;if(OLtimerid>0){clearTimeout(OLtimerid);OLtimerid=0;}}
}
function OLcursorOff(){
var o=(OLns4?over:over.style),pHt=OLns4?over.clip.height:over.offsetHeight,left=parseInt(o.left),top=parseInt(o.top),
right=left+o3_width,bottom=top+((OLbubblePI&&o3_bubble)?OLbubbleHt:pHt);
if(OLx<left||OLx>right||OLy<top||OLy>bottom)return true;return false;
}

/*
 REGISTRATION
*/
function OLsetRunTimeVar(){
if(OLrunTime.length)for(var k=0;k<OLrunTime.length;k++)OLrunTime[k]();
}
function OLparseCmdLine(pf,i,ar){
if(OLcmdLine.length){for(var k=0;k<OLcmdLine.length;k++){var j=OLcmdLine[k](pf,i,ar);if(j>-1){i=j;break;}}}return i;
}
function OLregCmds(c){
if(typeof c!='string')return;var pM=c.split(',');pMtr=pMtr.concat(pM);
for(var i=0;i<pM.length;i++)eval(pM[i].toUpperCase()+'='+pmCnt++);
}
function OLregRunTimeFunc(f){
if(typeof f=='object')OLrunTime=OLrunTime.concat(f);else OLrunTime[OLrunTime.length++]=f;
}
function OLregCmdLineFunc(f){
if(typeof f=='object')OLcmdLine=OLcmdLine.concat(f);else OLcmdLine[OLcmdLine.length++]=f;
}

OLloaded=1;
