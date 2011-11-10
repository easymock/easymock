/*
	Copyright (c) 2000, Derek Petillo
	All rights reserved.

	Redistribution and use in source and binary forms, with or without 
	modification, are permitted provided that the following conditions are
	met:

	Redistributions of source code must retain the above copyright notice,
	this list of conditions and the following disclaimer. 
	
	Redistributions in binary form must reproduce the above copyright 
	notice, this list of conditions and the following disclaimer in the 
	documentation and/or other materials provided with the distribution. 
	
	Neither the name of Praxis Software nor the names of its contributors 
	may be used to endorse or promote products derived from this software 
	without specific prior written permission.
	 
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
	IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
	TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
	PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
	OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
	SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
	LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
	DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
	THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
	OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/
function QueryString() {
	var data = [];
	this.Read = function() 
	{
		var aPairs, aTmp;
		var queryString = new String(window.location.search);
		queryString = queryString.substr(1, queryString.length); //remove "?"
		aPairs = queryString.split("&");	
		
		for (var i=0 ; i<aPairs.length; i++)
		{
			aTmp = aPairs[i].split("=");
			data[aTmp[0]] = aTmp[1];
		}
	}
	
	this.GetValue = function( key )
	{
		return data[key];
	}
	this.SetValue = function( key, value )
	{
		if (value == null)
			delete data[key];
		else 
			data[key] = value;
	}
	this.ToString = function()
	{
		var queryString = new String(""); 
		
		for (var key in data)
		{	
			if (queryString != "")
				queryString += "&"
			if (data[key])
				queryString += key + "=" + data[key];		
		}
		if (queryString.length > 0)
			return "?" + queryString;
		else
			return queryString;
	}
	this.Clear = function()
	{
		delete data;
		data = [];
	}
}


function Cookies() {
	var cookieData = [];
	
	this.Read = function()
	{
		var pairs = new String(window.document.cookie).split(";");	
		var tmp, cookieName, keyName;
		for (var i=0 ; i<pairs.length; i++)
		{
			tmp = pairs[i].split("=");
			
			if (tmp.length == 3)
			{
				cookieName = new String(tmp[0]);
				cookieName = cookieName.replace(" ", "");
				
				if (cookieData[cookieName] == null)
					cookieData[cookieName] = [];
				cookieData[cookieName][tmp[1]] = unescape(tmp[1]);
			}
			else //length = 2
			{
				keyName = tmp[0];
				keyName = keyName.replace(" ", "");
				if (keyName.substring(0,12)!="ASPSESSIONID") 
				{
					if (cookieData[""] == null)
						cookieData[""] = [];
					cookieData[""][keyName] = unescape(tmp[1]);
				}
			}	
		}	
		
	}
	
	this.GetValue = function( cookie, key )
	{
		if (cookieData[cookie] != null)
			return cookieData[cookie][key];
		else
			return null;
	}
	this.SetValue = function( cookie, key, value )
	{
		if (cookieData[cookie] == null)
			cookieData[cookie] = [];
		cookieData[cookie][key] = value;
	}
	this.Write = function()
	{
	
		var toWrite;
		var thisCookie;
		var expireDateKill = new Date();
		var expireDate = new Date();
		expireDate.setYear(expireDate.getFullYear() + 10);
		expireDateKill.setYear(expireDateKill.getFullYear() - 10);


		var pairs = new String(window.document.cookie).split(";");	
		var tmp, cookieName, keyName;
		for (var i=0 ; i<pairs.length; i++)
		{
			tmp = pairs[i].split("=");
			if (tmp.length == 3)	
			{		
				window.document.cookie = tmp[0] + "=" + tmp[1] + "='';expires=" + expireDateKill.toGMTString();
			}
			else
			{
				keyName = tmp[0];
				keyName = keyName.replace(" ", "");
				if (keyName.substring(0,12)!="ASPSESSIONID") 
					window.document.cookie = keyName + "='';expires=" + expireDateKill.toGMTString();
			}
		}

		for (var cookie in cookieData)
		{
			toWrite = "";
			thisCookie = cookieData[cookie];
			for (var key in thisCookie)
			{
				if (thisCookie[key] != null)
				{
					if (cookie == "")
						toWrite = key + "=" + thisCookie[key];
					else
						toWrite = cookie + "=" + key + "=" + escape(thisCookie[key]);						
					toWrite += "; expires=" + expireDate.toGMTString();
					window.document.cookie = toWrite;	
				}
			}
		}
	}
}
