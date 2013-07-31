/* author: Julius Seltenheim (mail@julius-seltenheim.com) */ 
window.Timeline=Timeline;Timeline.Visualisations={VerticalSmallBar:1,VerticalBigBar:2,VerticalMinimal:3};var js={},defaultConfig={};Timeline.prototype.defaultConfig=defaultConfig;function Timeline(a,b){this.config=b?b:this.defaultConfig;this.fromYear=a;this.visualisations=[];this.timelineEntries=[]}
Timeline.prototype.addVisualisation=function(a,b,c){var d=null;a==Timeline.Visualisations.VerticalSmallBar?d=new VisualisationVerticalSmallBar(this,b,c):a==Timeline.Visualisations.VerticalBigBar?d=new VisualisationVerticalBigBar(this,b,c):a==Timeline.Visualisations.VerticalMinimal&&(d=new VisualisationVerticalMinimal(this,b,c));if(null!=d)return this.visualisations.push(d),d;throw"Unrecognized Visualisation";};Timeline.prototype.update=function(){for(var a in this.visualisations)this.visualisations[a].update()};
Timeline.prototype.getFromYear=function(){return this.fromYear};Timeline.prototype.getLocation=function(){return this.location};Timeline.prototype.getTimelineEntries=function(){return this.timelineEntries};Timeline.prototype.getEntriesInTimeRange=function(a,b,c){var d=[],f;for(f in this.timelineEntries){var g=this.timelineEntries[f];(g.fromDate>=a&&g.fromDate<=b||g.toDate>=a&&g.toDate<=b||g.fromDate<a&&g.toDate>b)&&g!=c&&d.push(g)}return d};
Timeline.prototype.getTakenLevelsInTimeRange=function(a,b,c){a=this.getEntriesInTimeRange(a,b,c);b=[];for(var d in a)c=a[d],-1==b.indexOf(c.level)&&b.push(c.level);return b};Timeline.prototype.addTimelineEntry=function(a){this.timelineEntries.push(a);a.addListener(this);for(var b in this.visualisations)this.visualisations[b].onNewTimelineEntry(a)};
Timeline.prototype.onHTMLElementToTriggerHoverAdded=function(a,b){for(var c in this.visualisations)this.visualisations[c].onHTMLElementToTriggerHoverAdded(a,b)};function TimelineEntry(a,b,c,d){this.title=a;this.fromDate=b;this.toDate=c;this.color=d;this.level=0;this.listeners=[];this.highlightingHtmlElements=[]}TimelineEntry.prototype.getHash=function(){return this.title+"_"+this.fromDate.getTime()+"_"+this.toDate.getTime()};TimelineEntry.prototype.addListener=function(a){this.listeners.push(a)};
TimelineEntry.prototype.addHTMLElementToTriggerHover=function(a){this.highlightingHtmlElements.push(a);for(var b in this.listeners)this.listeners[b].onHTMLElementToTriggerHoverAdded(this,a)};var jsTimelineTooltip=null;function Tooltip(a,b){this.destroyExistingTooltip();jsTimelineTooltip=this;this.div=this.createDiv();var c=a.pageX;this.div.style.top=a.pageY+"px";this.div.style.left=c+10+"px";this.div.innerHTML=b;document.getElementsByTagName("body")[0].appendChild(this.div)}
Tooltip.prototype.createDiv=function(){var a=document.createElement("div");a.style.position="absolute";a.style.display="block";a.style.backgroundColor="black";a.style.color="white";a.style.font="Arial";a.style.fontSize="10px";a.style.padding="3px";return a};Tooltip.prototype.destroyExistingTooltip=function(){null!=jsTimelineTooltip&&document.getElementsByTagName("body")[0].removeChild(jsTimelineTooltip.div);jsTimelineTooltip=null};
function VisualisationBase(a,b,c){this.timeline=a;this.htmlElement=b;this.timelineEntryVisualisationMaps={};for(this.config=c?c:{};this.htmlElement.firstChild;)this.htmlElement.removeChild(this.location.firstChild);this.masterSvg=document.createElementNS("http://www.w3.org/2000/svg","svg");this.masterSvg.setAttribute("style","position:absolute;left:0px;top:0px;width:100%;height:100%;");this.id=(new Date).getTime();this.htmlElement.appendChild(this.masterSvg)}VisualisationBase.prototype.getWidth=function(){return this.htmlElement.clientWidth};
VisualisationBase.prototype.getHeight=function(){return this.htmlElement.clientHeight};VisualisationBase.prototype.getConfig=function(){return this.config};VisualisationBase.prototype.getHTMLElement=function(){return this.htmlElement};VisualisationBase.prototype.repaint=function(){throw"NotImplementedException";};VisualisationBase.prototype.getShapeForTimelineEntry=function(a){throw"NotImplementedException";};
VisualisationBase.prototype.onNewTimelineEntry=function(a){var b=this.getShapeForTimelineEntry(a);this.timelineEntryVisualisationMaps[a.getHash()]=b;for(var c in a.highlightingHtmlElements)this.onHTMLElementToTriggerHoverAdded(a,a.highlightingHtmlElements[c]);this.masterSvg.appendChild(b)};
VisualisationBase.prototype.onHTMLElementToTriggerHoverAdded=function(a,b){var c=this.timelineEntryVisualisationMaps[a.getHash()];addEvent(b,"mouseover",function(a){c.classList.add("hover")});addEvent(b,"mouseout",function(a){c.classList.remove("hover")})};defaultConfig={scale:{}};defaultConfig.scale.lineWidth=3;defaultConfig.scale.margin=5;defaultConfig.scale.backgroundColor="ffffff";defaultConfig.scale.fontSize=15;defaultConfig.scale.arrowHeadHeight=13;defaultConfig.scale.numbersMarginRight=20;
defaultConfig.drawToday=!0;defaultConfig.drawBaseLineYear=!0;defaultConfig.drawTickLabels=!0;defaultConfig.entries={};defaultConfig.entries.colors="f7c6c7 fad8c7 fef2c0 bfe5bf bfdadc c7def8 bfd4f2 d4c5f9".split(" ");
function VisualisationVerticalBigBar(a,b,c){VisualisationBase.call(this,a,b,c?c:this.defaultConfig);a=document.createElement("style");a.setAttribute("type","text/css");b=document.createTextNode(".js_timeline_entry.hover{opacity:0.5;} .js_timeline_entry{opacity:1;}");a.appendChild(b);this.masterSvg.appendChild(a);this.lastColor=0;this.repaint()}VisualisationVerticalBigBar.prototype=Object.create(VisualisationBase.prototype);VisualisationVerticalBigBar.prototype.defaultConfig=defaultConfig;
VisualisationVerticalBigBar.prototype.repaint=function(){this.updateScale();this.updateTicks();this.updateStartYearAndNowString();this.updateArrowHead();this.updateEntries()};VisualisationVerticalBigBar.prototype.getCenter=function(){return this.getWidth()/2};VisualisationVerticalBigBar.prototype.getNextColor=function(){var a=this.lastColor+1;this.config.entries.colors.length==a&&(a=0);this.lastColor=a;return this.config.entries.colors[a]};
VisualisationVerticalBigBar.prototype.getPosForDate=function(a){var b=(new Date(this.timeline.getFromYear(),0,1)).getTime(),c=(new Date).getTime()-b;a=(a.getTime()-b)/c;b=this.getTopOffsetForEntry();c=this.getHeightForEntry();return 1<=a?b:0>=a?b+c:c-c*a+b};VisualisationVerticalBigBar.prototype.getTopOffsetForEntry=function(){var a=this.getTopOffsetForScale();return a+=this.config.scale.arrowHeadHeight};VisualisationVerticalBigBar.prototype.getBottomOffsetForEntry=function(){return this.getBottomOffsetForScale()};
VisualisationVerticalBigBar.prototype.getHeightForEntry=function(){var a=this.getTopOffsetForEntry()+this.getBottomOffsetForScale();return this.getHeight()-a};VisualisationVerticalBigBar.prototype.getTopOffsetForScale=function(){var a=0;this.config.drawToday&&(a+=this.config.scale.fontSize+2);return a};VisualisationVerticalBigBar.prototype.getBottomOffsetForScale=function(){var a=3;this.config.drawBaseLineYear&&(a+=this.config.scale.fontSize+2);return a};
VisualisationVerticalBigBar.prototype.getHeightForScale=function(){var a=this.getTopOffsetForScale()+this.getBottomOffsetForScale();return this.getHeight()-a};
VisualisationVerticalBigBar.prototype.updateScale=function(){var a=this.getTopOffsetForEntry(),b=this.getHeight()-this.getBottomOffsetForEntry(),c=this.config.scale.lineWidth,d=this.config.scale.margin,f=this.getWidth(),g=d+c,d=f-(d+c);this.scaleLine1||(this.scaleLine1=document.createElementNS("http://www.w3.org/2000/svg","line"),this.masterSvg.appendChild(this.scaleLine1),this.scaleLine2=document.createElementNS("http://www.w3.org/2000/svg","line"),this.masterSvg.appendChild(this.scaleLine2),this.scaleBackground=
document.createElementNS("http://www.w3.org/2000/svg","rect"),this.masterSvg.appendChild(this.scaleBackground));this.scaleLine1.setAttribute("x1",g);this.scaleLine1.setAttribute("y1",a);this.scaleLine1.setAttribute("x2",g);this.scaleLine1.setAttribute("y2",b);this.scaleLine1.setAttribute("style","stroke:rgb(0,0,0);stroke-width:"+c);this.scaleLine2.setAttribute("x1",d);this.scaleLine2.setAttribute("y1",a);this.scaleLine2.setAttribute("x2",d);this.scaleLine2.setAttribute("y2",b);this.scaleLine2.setAttribute("style",
"stroke:rgb(0,0,0);stroke-width:"+c);this.scaleBackground.setAttribute("y",a);this.scaleBackground.setAttribute("x",g);this.scaleBackground.setAttribute("height",b-a);this.scaleBackground.setAttribute("width",d-g);this.scaleBackground.setAttribute("style","fill:#"+this.config.scale.backgroundColor+";");this.scaleBackground.setAttribute("class","js_timeline_entry")};
VisualisationVerticalBigBar.prototype.updateTicks=function(){var a=this.getHeightForEntry(),b=this.getTopOffsetForEntry(),c=this.config.drawTickLabels,d=this.config.scale.lineWidth,f=this.config.scale.margin,g=this.getWidth(),l=f+d,d=g-(f+d),f=(new Date(this.timeline.getFromYear(),0,1)).getTime(),f=((new Date).getTime()-f)/315576E5,g=a/f;if(this.tickSvgs)for(var e in this.tickSvgs)this.masterSvg.removeChild(this.tickSvgs[e]);this.tickSvgs=[];for(e=0;e<f;e+=1){var k=b+a-e*g,h=document.createElementNS("http://www.w3.org/2000/svg",
"line");h.setAttribute("x1",l);h.setAttribute("y1",k);h.setAttribute("x2",d);h.setAttribute("y2",k);h.setAttribute("style","stroke:rgb(0,0,0);stroke-width:2");this.tickSvgs.push(h);this.masterSvg.appendChild(h);c&&0!=e&&(h=document.createElementNS("http://www.w3.org/2000/svg","text"),h.setAttribute("x",d-this.config.scale.numbersMarginRight),h.setAttribute("y",k+10),h.setAttribute("fill","black"),h.setAttribute("font-size","10"),k=document.createTextNode(this.timeline.getFromYear()+e+""),h.appendChild(k),
this.tickSvgs.push(h),this.masterSvg.appendChild(h))}};
VisualisationVerticalBigBar.prototype.updateStartYearAndNowString=function(){if(this.labelSvgs)for(var a in this.labelSvgs)this.masterSvg.removeChild(this.labelSvgs[a]);this.labelSvgs=[];a=this.getCenter()-15;var b=this.config.scale.fontSize,c=0.3*b;if(this.config.drawToday){var d=document.createElementNS("http://www.w3.org/2000/svg","text");d.setAttribute("x",a);d.setAttribute("y",2.7*c);d.setAttribute("fill","black");d.setAttribute("font-size",b);var f=document.createTextNode("today");d.appendChild(f);
this.labelSvgs.push(d);this.masterSvg.appendChild(d)}this.config.drawBaseLineYear&&(d=document.createElementNS("http://www.w3.org/2000/svg","text"),d.setAttribute("x",a),d.setAttribute("y",this.getHeight()-c),d.setAttribute("fill","black"),d.setAttribute("font-size",b),a=document.createTextNode(this.timeline.getFromYear()),d.appendChild(a),this.labelSvgs.push(d),this.masterSvg.appendChild(d))};
VisualisationVerticalBigBar.prototype.updateArrowHead=function(){var a=this.getTopOffsetForScale(),b=this.getWidth()-1,c=this.getCenter(),d=this.config.scale.arrowHeadHeight,f=d+a+3,d=d+a+5;this.arrowHeadSvg||(this.arrowHeadSvg=document.createElementNS("http://www.w3.org/2000/svg","polyline"),this.masterSvg.appendChild(this.arrowHeadSvg));this.arrowHeadSvg.setAttribute("points","1,"+f+" "+c+","+a+" "+b+","+d);this.arrowHeadSvg.setAttribute("style","fill:none;stroke:black;stroke-width:2")};
VisualisationVerticalBigBar.prototype.updateEntries=function(){var a=this.timeline.getTimelineEntries(),b;for(b in this.timelineEntryVisualisationMaps)this.masterSvg.removeChild(this.timelineEntryVisualisationMaps[b]);for(b in a)this.onNewTimelineEntry(a[b])};
VisualisationVerticalBigBar.prototype.getShapeForTimelineEntry=function(a){for(var b=this.timeline.getTakenLevelsInTimeRange(a.fromDate,a.toDate,a),c=0;-1!=b.indexOf(c);)c+=1;b=c;a.level=c;a.color||(a.color=this.getNextColor());var c=a.color,d=this.getPosForDate(a.fromDate),f=this.getPosForDate(a.toDate),d=d-f,g=this.config.scale.lineWidth,l=this.config.scale.margin;this.getWidth();var b=l+g+6*b,e=document.createElementNS("http://www.w3.org/2000/svg","rect");e.setAttribute("y",f);e.setAttribute("x",
b);e.setAttribute("height",d);e.setAttribute("width",5);e.setAttribute("style","fill:#"+c+";stroke:black;stroke-width:1;pointer-events:all;");e.setAttribute("class","js_timeline_entry");e.onmouseover=function(b){a.tooltip=new Tooltip(b,a.title);e.classList.add("hover")};e.onmouseout=function(){a.tooltip.destroyExistingTooltip();e.classList.remove("hover")};return e};function addEvent(a,b,c){a.addEventListener?a.addEventListener(b,c,!1):a.attachEvent&&a.attachEvent("on"+b,c)}defaultConfig={scale:{}};
defaultConfig.scale.lineWidth=3;defaultConfig.scale.margin=5;defaultConfig.scale.backgroundColor="ffffff";defaultConfig.scale.fontSize=15;defaultConfig.scale.numbersMarginRight=20;defaultConfig.drawBaseLineYear=!0;defaultConfig.drawTickLabels=!0;defaultConfig.entries={};defaultConfig.entries.colors="f7c6c7 fad8c7 fef2c0 bfe5bf bfdadc c7def8 bfd4f2 d4c5f9".split(" ");
function VisualisationVerticalMinimal(a,b,c){VisualisationBase.call(this,a,b,c?c:this.defaultConfig);a=document.createElement("style");a.setAttribute("type","text/css");b=document.createTextNode(".js_timeline_entry_"+this.id+".hover{stroke-width:1;stroke:black;}");a.appendChild(b);this.masterSvg.appendChild(a);this.lastColor=0;this.repaint()}VisualisationVerticalMinimal.prototype=Object.create(VisualisationBase.prototype);VisualisationVerticalMinimal.prototype.defaultConfig=defaultConfig;
VisualisationVerticalMinimal.prototype.repaint=function(){this.updateTicks();this.updateStartYear();this.updateEntries()};VisualisationVerticalMinimal.prototype.getCenter=function(){return this.getWidth()/2};VisualisationVerticalMinimal.prototype.getNextColor=function(){var a=this.lastColor+1;this.config.entries.colors.length==a&&(a=0);this.lastColor=a;return this.config.entries.colors[a]};
VisualisationVerticalMinimal.prototype.getPosForDate=function(a){var b=(new Date(this.timeline.getFromYear(),0,1)).getTime(),c=(new Date).getTime()-b;a=(a.getTime()-b)/c;b=this.getTopOffsetForEntry();c=this.getHeightForEntry();return 1<=a?b:0>=a?b+c:c-c*a+b};VisualisationVerticalMinimal.prototype.getTopOffsetForEntry=function(){return this.getTopOffsetForScale()};VisualisationVerticalMinimal.prototype.getBottomOffsetForEntry=function(){return this.getBottomOffsetForScale()};
VisualisationVerticalMinimal.prototype.getHeightForEntry=function(){var a=this.getTopOffsetForEntry()+this.getBottomOffsetForScale();return this.getHeight()-a};VisualisationVerticalMinimal.prototype.getTopOffsetForScale=function(){return 0};VisualisationVerticalMinimal.prototype.getBottomOffsetForScale=function(){var a=3;this.config.drawBaseLineYear&&(a+=this.config.scale.fontSize+2);return a};
VisualisationVerticalMinimal.prototype.getHeightForScale=function(){var a=this.getTopOffsetForScale()+this.getBottomOffsetForScale();return this.getHeight()-a};
VisualisationVerticalMinimal.prototype.updateTicks=function(){var a=this.getHeightForEntry(),b=this.getTopOffsetForEntry(),c=this.config.drawTickLabels,d=this.config.scale.lineWidth,f=this.config.scale.margin,g=this.getWidth(),l=f+d,d=g-(f+d),f=(new Date(this.timeline.getFromYear(),0,1)).getTime(),f=((new Date).getTime()-f)/315576E5,g=a/f;if(this.tickSvgs)for(var e in this.tickSvgs)this.masterSvg.removeChild(this.tickSvgs[e]);this.tickSvgs=[];for(e=0;e<f;e+=1){var k=b+a-e*g,h=document.createElementNS("http://www.w3.org/2000/svg",
"line");h.setAttribute("x1",l);h.setAttribute("y1",k);h.setAttribute("x2",d);h.setAttribute("y2",k);h.setAttribute("style","stroke:rgb(0,0,0);stroke-width:1");this.tickSvgs.push(h);this.masterSvg.appendChild(h);c&&0!=e&&(h=document.createElementNS("http://www.w3.org/2000/svg","text"),h.setAttribute("x",d-this.config.scale.numbersMarginRight),h.setAttribute("y",k+10),h.setAttribute("fill","black"),h.setAttribute("font-size","10"),k=document.createTextNode(this.timeline.getFromYear()+e+""),h.appendChild(k),
this.tickSvgs.push(h),this.masterSvg.appendChild(h))}};
VisualisationVerticalMinimal.prototype.updateStartYear=function(){if(this.labelSvgs)for(var a in this.labelSvgs)this.masterSvg.removeChild(this.labelSvgs[a]);this.labelSvgs=[];var b=this.getCenter()-15,c=this.config.scale.fontSize,d=0.3*c;this.config.drawBaseLineYear&&(a=document.createElementNS("http://www.w3.org/2000/svg","text"),a.setAttribute("x",b),a.setAttribute("y",this.getHeight()-d),a.setAttribute("fill","black"),a.setAttribute("font-size",c),b=document.createTextNode(this.timeline.getFromYear()),
a.appendChild(b),this.labelSvgs.push(a),this.masterSvg.appendChild(a))};VisualisationVerticalMinimal.prototype.updateEntries=function(){var a=this.timeline.getTimelineEntries(),b;for(b in this.timelineEntryVisualisationMaps)this.masterSvg.removeChild(this.timelineEntryVisualisationMaps[b]);for(b in a)this.onNewTimelineEntry(a[b])};
VisualisationVerticalMinimal.prototype.getShapeForTimelineEntry=function(a){for(var b=this.timeline.getTakenLevelsInTimeRange(a.fromDate,a.toDate,a),c=0;-1!=b.indexOf(c);)c+=1;b=c;a.level=c;a.color||(a.color=this.getNextColor());var c=a.color,d=this.getPosForDate(a.fromDate),f=this.getPosForDate(a.toDate),d=d-f,g=this.config.scale.lineWidth,l=this.config.scale.margin;this.getWidth();var b=l+g+6*b,e=document.createElementNS("http://www.w3.org/2000/svg","rect");e.setAttribute("y",f);e.setAttribute("x",
b);e.setAttribute("height",d);e.setAttribute("width",5);e.setAttribute("style","fill:#"+c+";pointer-events:all;");e.setAttribute("class","js_timeline_entry_"+this.id);e.onmouseover=function(b){a.tooltip=new Tooltip(b,a.title);e.classList.add("hover")};e.onmouseout=function(){a.tooltip.destroyExistingTooltip();e.classList.remove("hover")};return e};function addEvent(a,b,c){a.addEventListener?a.addEventListener(b,c,!1):a.attachEvent&&a.attachEvent("on"+b,c)}defaultConfig={scale:{}};
defaultConfig.scale.lineWidth=3;defaultConfig.scale.fontSize=15;defaultConfig.scale.arrowHeadHeight=13;defaultConfig.scale.arrowHeadWidth=20;defaultConfig.drawToday=!0;defaultConfig.drawBaseLineYear=!0;defaultConfig.drawTickLabels=!0;defaultConfig.entries={};defaultConfig.entries.colors="f7c6c7 fad8c7 fef2c0 bfe5bf bfdadc c7def8 bfd4f2 d4c5f9".split(" ");
function VisualisationVerticalSmallBar(a,b,c){VisualisationBase.call(this,a,b,c?c:this.defaultConfig);a=document.createElement("style");a.setAttribute("type","text/css");b=document.createTextNode(".js_timeline_entry.hover{opacity:0.5;} .js_timeline_entry{opacity:1;}");a.appendChild(b);this.masterSvg.appendChild(a);this.lastColor=0;this.repaint()}VisualisationVerticalSmallBar.prototype=Object.create(VisualisationBase.prototype);VisualisationVerticalSmallBar.prototype.defaultConfig=defaultConfig;
VisualisationVerticalSmallBar.prototype.repaint=function(){this.updateScale();this.updateTicks();this.updateStartYearAndNowString();this.updateArrowHead();this.updateEntries()};VisualisationVerticalSmallBar.prototype.getCenter=function(){return 0.9*(this.getWidth()/2)};VisualisationVerticalSmallBar.prototype.getNextColor=function(){var a=this.lastColor+1;this.config.entries.colors.length==a&&(a=0);this.lastColor=a;return this.config.entries.colors[a]};
VisualisationVerticalSmallBar.prototype.getPosForDate=function(a){var b=(new Date(this.timeline.getFromYear(),0,1)).getTime(),c=(new Date).getTime()-b;a=(a.getTime()-b)/c;b=this.getTopOffsetForEntry();c=this.getHeightForEntry();return 1<=a?b:0>=a?b+c:c-c*a+b};VisualisationVerticalSmallBar.prototype.getTopOffsetForEntry=function(){var a=this.getTopOffsetForScale();return a+=this.config.scale.arrowHeadHeight+2};VisualisationVerticalSmallBar.prototype.getBottomOffsetForEntry=function(){return this.getBottomOffsetForScale()};
VisualisationVerticalSmallBar.prototype.getHeightForEntry=function(){var a=this.getTopOffsetForEntry()+this.getBottomOffsetForScale();return this.getHeight()-a};VisualisationVerticalSmallBar.prototype.getTopOffsetForScale=function(){var a=0;this.config.drawToday&&(a+=this.config.scale.fontSize+2);return a};VisualisationVerticalSmallBar.prototype.getBottomOffsetForScale=function(){var a=3;this.config.drawBaseLineYear&&(a+=this.config.scale.fontSize+2);return a};
VisualisationVerticalSmallBar.prototype.getHeightForScale=function(){var a=this.getTopOffsetForScale()+this.getBottomOffsetForScale();return this.getHeight()-a};
VisualisationVerticalSmallBar.prototype.updateScale=function(){var a=this.getTopOffsetForScale(),b=this.getHeight()-this.getBottomOffsetForScale(),c=this.config.scale.lineWidth,d=this.getCenter();this.scaleLine||(this.scaleLine=document.createElementNS("http://www.w3.org/2000/svg","line"),this.masterSvg.appendChild(this.scaleLine));this.scaleLine.setAttribute("x1",d);this.scaleLine.setAttribute("y1",a);this.scaleLine.setAttribute("x2",d);this.scaleLine.setAttribute("y2",b);this.scaleLine.setAttribute("style",
"stroke:rgb(0,0,0);stroke-width:"+c)};
VisualisationVerticalSmallBar.prototype.updateTicks=function(){var a=this.getHeightForEntry(),b=this.getTopOffsetForEntry(),c=this.config.drawTickLabels,d=this.getCenter(),f=(new Date(this.timeline.getFromYear(),0,1)).getTime(),f=((new Date).getTime()-f)/315576E5,g=a/f;if(this.tickSvgs)for(var l in this.tickSvgs)this.masterSvg.removeChild(this.tickSvgs[l]);this.tickSvgs=[];for(l=0;l<f;l+=1){var e=b+a-l*g,k=document.createElementNS("http://www.w3.org/2000/svg","line");k.setAttribute("x1",d-8);k.setAttribute("y1",
e);k.setAttribute("x2",d+8);k.setAttribute("y2",e);k.setAttribute("style","stroke:rgb(0,0,0);stroke-width:2");this.tickSvgs.push(k);this.masterSvg.appendChild(k);c&&0!=l&&(k=document.createElementNS("http://www.w3.org/2000/svg","text"),k.setAttribute("x",d+3),k.setAttribute("y",e+10),k.setAttribute("fill","black"),k.setAttribute("font-size","10"),e=document.createTextNode(this.timeline.getFromYear()+l+""),k.appendChild(e),this.tickSvgs.push(k),this.masterSvg.appendChild(k))}};
VisualisationVerticalSmallBar.prototype.updateStartYearAndNowString=function(){if(this.labelSvgs)for(var a in this.labelSvgs)this.masterSvg.removeChild(this.labelSvgs[a]);this.labelSvgs=[];a=this.getCenter()-15;var b=this.config.scale.fontSize,c=0.3*b;if(this.config.drawToday){var d=document.createElementNS("http://www.w3.org/2000/svg","text");d.setAttribute("x",a);d.setAttribute("y",2.7*c);d.setAttribute("fill","black");d.setAttribute("font-size",b);var f=document.createTextNode("today");d.appendChild(f);
this.labelSvgs.push(d);this.masterSvg.appendChild(d)}this.config.drawBaseLineYear&&(d=document.createElementNS("http://www.w3.org/2000/svg","text"),d.setAttribute("x",a),d.setAttribute("y",this.getHeight()-c),d.setAttribute("fill","black"),d.setAttribute("font-size",b),a=document.createTextNode(this.timeline.getFromYear()),d.appendChild(a),this.labelSvgs.push(d),this.masterSvg.appendChild(d))};
VisualisationVerticalSmallBar.prototype.updateArrowHead=function(){var a=this.getTopOffsetForScale(),b=this.config.scale.lineWidth,c=this.getCenter(),d=this.config.scale.arrowHeadWidth/2,f=this.config.scale.arrowHeadHeight,g=c-d,l=f+a,d=c+d,f=f+a;this.arrowHeadSvg||(this.arrowHeadSvg=document.createElementNS("http://www.w3.org/2000/svg","polyline"),this.masterSvg.appendChild(this.arrowHeadSvg));this.arrowHeadSvg.setAttribute("points",g+","+l+" "+c+","+a+" "+d+","+f);this.arrowHeadSvg.setAttribute("style",
"fill:none;stroke:black;stroke-width:"+b)};VisualisationVerticalSmallBar.prototype.updateEntries=function(){var a=this.timeline.getTimelineEntries(),b;for(b in this.timelineEntryVisualisationMaps)this.masterSvg.removeChild(this.timelineEntryVisualisationMaps[b]);for(b in a)this.onNewTimelineEntry(a[b])};
VisualisationVerticalSmallBar.prototype.getShapeForTimelineEntry=function(a){var b=this.timeline.getTakenLevelsInTimeRange(a.fromDate,a.toDate,a);a.color||(a.color=this.getNextColor());for(var c=a.color,d=0;-1!=b.indexOf(d);)d+=1;a.level=d;var f=this.getPosForDate(a.fromDate),b=this.getPosForDate(a.toDate),f=f-b;this.getWidth();var g=this.getCenter(),l=0,l=0==d%2?g+1+6*(d/2):g-7-6*((d-1)/2),e=document.createElementNS("http://www.w3.org/2000/svg","rect");e.setAttribute("y",b);e.setAttribute("x",l);
e.setAttribute("height",f);e.setAttribute("width",5);e.setAttribute("style","fill:#"+c+";stroke:black;stroke-width:1;pointer-events:all;");e.setAttribute("class","js_timeline_entry");e.onmouseover=function(b){a.tooltip=new Tooltip(b,a.title);e.classList.add("hover")};e.onmouseout=function(){a.tooltip.destroyExistingTooltip();e.classList.remove("hover")};return e};function addEvent(a,b,c){a.addEventListener?a.addEventListener(b,c,!1):a.attachEvent&&a.attachEvent("on"+b,c)};
