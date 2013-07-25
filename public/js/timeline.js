/* author: Julius Seltenheim (mail@julius-seltenheim.com) */ 
window.Timeline=Timeline;Timeline.Visualisations={VerticalSmallBar:1,VerticalBigBar:2};var js={},defaultConfig={};Timeline.prototype.defaultConfig=defaultConfig;function Timeline(a,b){this.config=b?b:this.defaultConfig;this.fromYear=a;this.visualisations=[];this.timelineEntries=[]}
Timeline.prototype.addVisualisation=function(a,b,d){var c=null;a==Timeline.Visualisations.VerticalSmallBar?c=new VisualisationVerticalSmallBar(this,b,d):a==Timeline.Visualisations.VerticalBigBar&&(c=new VisualisationVerticalBigBar(this,b,d));if(null!=c)return this.visualisations.push(c),c;throw"Unrecognized Visualisation";};Timeline.prototype.update=function(){for(var a in this.visualisations)this.visualisations[a].update()};Timeline.prototype.getFromYear=function(){return this.fromYear};
Timeline.prototype.getLocation=function(){return this.location};Timeline.prototype.getTimelineEntries=function(){return this.timelineEntries};Timeline.prototype.getEntriesInTimeRange=function(a,b,d){var c=[],f;for(f in this.timelineEntries){var g=this.timelineEntries[f];(g.fromDate>=a&&g.fromDate<=b||g.toDate>=a&&g.toDate<=b||g.fromDate<a&&g.toDate>b)&&g!=d&&c.push(g)}return c};
Timeline.prototype.getTakenLevelsInTimeRange=function(a,b,d){a=this.getEntriesInTimeRange(a,b,d);b=[];for(var c in a)d=a[c],-1==b.indexOf(d.level)&&b.push(d.level);return b};Timeline.prototype.addTimelineEntry=function(a){this.timelineEntries.push(a);a.addListener(this);for(var b in this.visualisations)this.visualisations[b].onNewTimelineEntry(a)};
Timeline.prototype.onHTMLElementToTriggerHoverAdded=function(a,b){for(var d in this.visualisations)this.visualisations[d].onHTMLElementToTriggerHoverAdded(a,b)};function TimelineEntry(a,b,d,c){this.title=a;this.fromDate=b;this.toDate=d;this.color=c;this.level=0;this.listeners=[];this.highlightingHtmlElements=[]}TimelineEntry.prototype.getHash=function(){return this.title+"_"+this.fromDate.getTime()+"_"+this.toDate.getTime()};TimelineEntry.prototype.addListener=function(a){this.listeners.push(a)};
TimelineEntry.prototype.addHTMLElementToTriggerHover=function(a){this.highlightingHtmlElements.push(a);for(var b in this.listeners)this.listeners[b].onHTMLElementToTriggerHoverAdded(this,a)};var jsTimelineTooltip=null;function Tooltip(a,b){this.destroyExistingTooltip();jsTimelineTooltip=this;this.div=this.createDiv();var d=a.pageX;this.div.style.top=a.pageY+"px";this.div.style.left=d+10+"px";this.div.innerHTML=b;document.getElementsByTagName("body")[0].appendChild(this.div)}
Tooltip.prototype.createDiv=function(){var a=document.createElement("div");a.style.position="absolute";a.style.display="block";a.style.backgroundColor="black";a.style.color="white";a.style.font="Arial";a.style.fontSize="10px";a.style.padding="3px";return a};Tooltip.prototype.destroyExistingTooltip=function(){null!=jsTimelineTooltip&&document.getElementsByTagName("body")[0].removeChild(jsTimelineTooltip.div);jsTimelineTooltip=null};
function VisualisationBase(a,b,d){this.timeline=a;this.htmlElement=b;this.timelineEntryVisualisationMaps={};for(this.config=d?d:{};this.htmlElement.firstChild;)this.htmlElement.removeChild(this.location.firstChild);this.masterSvg=document.createElementNS("http://www.w3.org/2000/svg","svg");this.masterSvg.setAttribute("style","position:absolute;left:0px;top:0px;width:100%;height:100%;");a=document.createElement("style");a.setAttribute("type","text/css");b=document.createTextNode(".js_timeline_entry.hover{opacity:0.5;} .js_timeline_entry{opacity:1;}");
a.appendChild(b);this.masterSvg.appendChild(a);this.htmlElement.appendChild(this.masterSvg)}VisualisationBase.prototype.getWidth=function(){return this.htmlElement.clientWidth};VisualisationBase.prototype.getHeight=function(){return this.htmlElement.clientHeight};VisualisationBase.prototype.getConfig=function(){return this.config};VisualisationBase.prototype.getHTMLElement=function(){return this.htmlElement};VisualisationBase.prototype.repaint=function(){throw"NotImplementedException";};
VisualisationBase.prototype.getShapeForTimelineEntry=function(a){throw"NotImplementedException";};VisualisationBase.prototype.onNewTimelineEntry=function(a){var b=this.getShapeForTimelineEntry(a);this.timelineEntryVisualisationMaps[a.getHash()]=b;for(var d in a.highlightingHtmlElements)this.onHTMLElementToTriggerHoverAdded(a,a.highlightingHtmlElements[d]);this.masterSvg.appendChild(b)};
VisualisationBase.prototype.onHTMLElementToTriggerHoverAdded=function(a,b){var d=this.timelineEntryVisualisationMaps[a.getHash()];addEvent(b,"mouseover",function(a){d.classList.add("hover")});addEvent(b,"mouseout",function(a){d.classList.remove("hover")})};defaultConfig={scale:{}};defaultConfig.scale.lineWidth=3;defaultConfig.scale.margin=5;defaultConfig.scale.backgroundColor="ffffff";defaultConfig.scale.fontSize=15;defaultConfig.scale.arrowHeadHeight=13;defaultConfig.scale.numbersMarginRight=20;
defaultConfig.drawToday=!0;defaultConfig.drawBaseLineYear=!0;defaultConfig.drawTickLabels=!0;defaultConfig.entries={};defaultConfig.entries.colors="f7c6c7 fad8c7 fef2c0 bfe5bf bfdadc c7def8 bfd4f2 d4c5f9".split(" ");function VisualisationVerticalBigBar(a,b,d){VisualisationBase.call(this,a,b,d?d:this.defaultConfig);this.lastColor=0;this.repaint()}VisualisationVerticalBigBar.prototype=Object.create(VisualisationBase.prototype);VisualisationVerticalBigBar.prototype.defaultConfig=defaultConfig;
VisualisationVerticalBigBar.prototype.repaint=function(){this.updateScale();this.updateTicks();this.updateStartYearAndNowString();this.updateArrowHead();this.updateEntries()};VisualisationVerticalBigBar.prototype.getCenter=function(){return this.getWidth()/2};VisualisationVerticalBigBar.prototype.getNextColor=function(){var a=this.lastColor+1;this.config.entries.colors.length==a&&(a=0);this.lastColor=a;return this.config.entries.colors[a]};
VisualisationVerticalBigBar.prototype.getPosForDate=function(a){var b=(new Date(this.timeline.getFromYear(),0,1)).getTime(),d=(new Date).getTime()-b;a=(a.getTime()-b)/d;b=this.getTopOffsetForEntry();d=this.getHeightForEntry();return 1<=a?b:0>=a?b+d:d-d*a+b};VisualisationVerticalBigBar.prototype.getTopOffsetForEntry=function(){var a=this.getTopOffsetForScale();return a+=this.config.scale.arrowHeadHeight};VisualisationVerticalBigBar.prototype.getBottomOffsetForEntry=function(){return this.getBottomOffsetForScale()};
VisualisationVerticalBigBar.prototype.getHeightForEntry=function(){var a=this.getTopOffsetForEntry()+this.getBottomOffsetForScale();return this.getHeight()-a};VisualisationVerticalBigBar.prototype.getTopOffsetForScale=function(){var a=0;this.config.drawToday&&(a+=this.config.scale.fontSize+2);return a};VisualisationVerticalBigBar.prototype.getBottomOffsetForScale=function(){var a=3;this.config.drawBaseLineYear&&(a+=this.config.scale.fontSize+2);return a};
VisualisationVerticalBigBar.prototype.getHeightForScale=function(){var a=this.getTopOffsetForScale()+this.getBottomOffsetForScale();return this.getHeight()-a};
VisualisationVerticalBigBar.prototype.updateScale=function(){var a=this.getTopOffsetForEntry(),b=this.getHeight()-this.getBottomOffsetForEntry(),d=this.config.scale.lineWidth,c=this.config.scale.margin,f=this.getWidth(),g=c+d,c=f-(c+d);this.scaleLine1||(this.scaleLine1=document.createElementNS("http://www.w3.org/2000/svg","line"),this.masterSvg.appendChild(this.scaleLine1),this.scaleLine2=document.createElementNS("http://www.w3.org/2000/svg","line"),this.masterSvg.appendChild(this.scaleLine2),this.scaleBackground=
document.createElementNS("http://www.w3.org/2000/svg","rect"),this.masterSvg.appendChild(this.scaleBackground));this.scaleLine1.setAttribute("x1",g);this.scaleLine1.setAttribute("y1",a);this.scaleLine1.setAttribute("x2",g);this.scaleLine1.setAttribute("y2",b);this.scaleLine1.setAttribute("style","stroke:rgb(0,0,0);stroke-width:"+d);this.scaleLine2.setAttribute("x1",c);this.scaleLine2.setAttribute("y1",a);this.scaleLine2.setAttribute("x2",c);this.scaleLine2.setAttribute("y2",b);this.scaleLine2.setAttribute("style",
"stroke:rgb(0,0,0);stroke-width:"+d);this.scaleBackground.setAttribute("y",a);this.scaleBackground.setAttribute("x",g);this.scaleBackground.setAttribute("height",b-a);this.scaleBackground.setAttribute("width",c-g);this.scaleBackground.setAttribute("style","fill:#"+this.config.scale.backgroundColor+";");this.scaleBackground.setAttribute("class","js_timeline_entry")};
VisualisationVerticalBigBar.prototype.updateTicks=function(){var a=this.getHeightForEntry(),b=this.getTopOffsetForEntry(),d=this.config.drawTickLabels,c=this.config.scale.lineWidth,f=this.config.scale.margin,g=this.getWidth(),k=f+c,c=g-(f+c),f=(new Date(this.timeline.getFromYear(),0,1)).getTime(),f=((new Date).getTime()-f)/315576E5,g=a/f;if(this.tickSvgs)for(var e in this.tickSvgs)this.masterSvg.removeChild(this.tickSvgs[e]);this.tickSvgs=[];for(e=0;e<f;e+=1){var h=b+a-e*g,l=document.createElementNS("http://www.w3.org/2000/svg",
"line");l.setAttribute("x1",k);l.setAttribute("y1",h);l.setAttribute("x2",c);l.setAttribute("y2",h);l.setAttribute("style","stroke:rgb(0,0,0);stroke-width:2");this.tickSvgs.push(l);this.masterSvg.appendChild(l);d&&0!=e&&(l=document.createElementNS("http://www.w3.org/2000/svg","text"),l.setAttribute("x",c-this.config.scale.numbersMarginRight),l.setAttribute("y",h+10),l.setAttribute("fill","black"),l.setAttribute("font-size","10"),h=document.createTextNode(this.timeline.getFromYear()+e+""),l.appendChild(h),
this.tickSvgs.push(l),this.masterSvg.appendChild(l))}};
VisualisationVerticalBigBar.prototype.updateStartYearAndNowString=function(){if(this.labelSvgs)for(var a in this.labelSvgs)this.masterSvg.removeChild(this.labelSvgs[a]);this.labelSvgs=[];a=this.getCenter()-15;var b=this.config.scale.fontSize,d=0.3*b;if(this.config.drawToday){var c=document.createElementNS("http://www.w3.org/2000/svg","text");c.setAttribute("x",a);c.setAttribute("y",2.7*d);c.setAttribute("fill","black");c.setAttribute("font-size",b);var f=document.createTextNode("today");c.appendChild(f);
this.labelSvgs.push(c);this.masterSvg.appendChild(c)}this.config.drawBaseLineYear&&(c=document.createElementNS("http://www.w3.org/2000/svg","text"),c.setAttribute("x",a),c.setAttribute("y",this.getHeight()-d),c.setAttribute("fill","black"),c.setAttribute("font-size",b),a=document.createTextNode(this.timeline.getFromYear()),c.appendChild(a),this.labelSvgs.push(c),this.masterSvg.appendChild(c))};
VisualisationVerticalBigBar.prototype.updateArrowHead=function(){var a=this.getTopOffsetForScale(),b=this.getWidth()-1,d=this.getCenter(),c=this.config.scale.arrowHeadHeight,f=c+a+3,c=c+a+5;this.arrowHeadSvg||(this.arrowHeadSvg=document.createElementNS("http://www.w3.org/2000/svg","polyline"),this.masterSvg.appendChild(this.arrowHeadSvg));this.arrowHeadSvg.setAttribute("points","1,"+f+" "+d+","+a+" "+b+","+c);this.arrowHeadSvg.setAttribute("style","fill:none;stroke:black;stroke-width:2")};
VisualisationVerticalBigBar.prototype.updateEntries=function(){var a=this.timeline.getTimelineEntries(),b;for(b in this.timelineEntryVisualisationMaps)this.masterSvg.removeChild(this.timelineEntryVisualisationMaps[b]);for(b in a)this.onNewTimelineEntry(a[b])};
VisualisationVerticalBigBar.prototype.getShapeForTimelineEntry=function(a){for(var b=this.timeline.getTakenLevelsInTimeRange(a.fromDate,a.toDate,a),d=0;-1!=b.indexOf(d);)d+=1;b=d;a.level=d;a.color||(a.color=this.getNextColor());var d=a.color,c=this.getPosForDate(a.fromDate),f=this.getPosForDate(a.toDate),c=c-f,g=this.config.scale.lineWidth,k=this.config.scale.margin;this.getWidth();var b=k+g+6*b,e=document.createElementNS("http://www.w3.org/2000/svg","rect");e.setAttribute("y",f);e.setAttribute("x",
b);e.setAttribute("height",c);e.setAttribute("width",5);e.setAttribute("style","fill:#"+d+";stroke:black;stroke-width:1;pointer-events:all;");e.setAttribute("class","js_timeline_entry");e.onmouseover=function(b){a.tooltip=new Tooltip(b,a.title);e.classList.add("hover")};e.onmouseout=function(){a.tooltip.destroyExistingTooltip();e.classList.remove("hover")};return e};function addEvent(a,b,d){a.addEventListener?a.addEventListener(b,d,!1):a.attachEvent&&a.attachEvent("on"+b,d)}defaultConfig={scale:{}};
defaultConfig.scale.lineWidth=3;defaultConfig.scale.fontSize=15;defaultConfig.scale.arrowHeadHeight=13;defaultConfig.scale.arrowHeadWidth=20;defaultConfig.drawToday=!0;defaultConfig.drawBaseLineYear=!0;defaultConfig.drawTickLabels=!0;defaultConfig.entries={};defaultConfig.entries.colors="f7c6c7 fad8c7 fef2c0 bfe5bf bfdadc c7def8 bfd4f2 d4c5f9".split(" ");function VisualisationVerticalSmallBar(a,b,d){VisualisationBase.call(this,a,b,d?d:this.defaultConfig);this.lastColor=0;this.repaint()}
VisualisationVerticalSmallBar.prototype=Object.create(VisualisationBase.prototype);VisualisationVerticalSmallBar.prototype.defaultConfig=defaultConfig;VisualisationVerticalSmallBar.prototype.repaint=function(){this.updateScale();this.updateTicks();this.updateStartYearAndNowString();this.updateArrowHead();this.updateEntries()};VisualisationVerticalSmallBar.prototype.getCenter=function(){return 0.9*(this.getWidth()/2)};
VisualisationVerticalSmallBar.prototype.getNextColor=function(){var a=this.lastColor+1;this.config.entries.colors.length==a&&(a=0);this.lastColor=a;return this.config.entries.colors[a]};VisualisationVerticalSmallBar.prototype.getPosForDate=function(a){var b=(new Date(this.timeline.getFromYear(),0,1)).getTime(),d=(new Date).getTime()-b;a=(a.getTime()-b)/d;b=this.getTopOffsetForEntry();d=this.getHeightForEntry();return 1<=a?b:0>=a?b+d:d-d*a+b};
VisualisationVerticalSmallBar.prototype.getTopOffsetForEntry=function(){var a=this.getTopOffsetForScale();return a+=this.config.scale.arrowHeadHeight+2};VisualisationVerticalSmallBar.prototype.getBottomOffsetForEntry=function(){return this.getBottomOffsetForScale()};VisualisationVerticalSmallBar.prototype.getHeightForEntry=function(){var a=this.getTopOffsetForEntry()+this.getBottomOffsetForScale();return this.getHeight()-a};
VisualisationVerticalSmallBar.prototype.getTopOffsetForScale=function(){var a=0;this.config.drawToday&&(a+=this.config.scale.fontSize+2);return a};VisualisationVerticalSmallBar.prototype.getBottomOffsetForScale=function(){var a=3;this.config.drawBaseLineYear&&(a+=this.config.scale.fontSize+2);return a};VisualisationVerticalSmallBar.prototype.getHeightForScale=function(){var a=this.getTopOffsetForScale()+this.getBottomOffsetForScale();return this.getHeight()-a};
VisualisationVerticalSmallBar.prototype.updateScale=function(){var a=this.getTopOffsetForScale(),b=this.getHeight()-this.getBottomOffsetForScale(),d=this.config.scale.lineWidth,c=this.getCenter();this.scaleLine||(this.scaleLine=document.createElementNS("http://www.w3.org/2000/svg","line"),this.masterSvg.appendChild(this.scaleLine));this.scaleLine.setAttribute("x1",c);this.scaleLine.setAttribute("y1",a);this.scaleLine.setAttribute("x2",c);this.scaleLine.setAttribute("y2",b);this.scaleLine.setAttribute("style",
"stroke:rgb(0,0,0);stroke-width:"+d)};
VisualisationVerticalSmallBar.prototype.updateTicks=function(){var a=this.getHeightForEntry(),b=this.getTopOffsetForEntry(),d=this.config.drawTickLabels,c=this.getCenter(),f=(new Date(this.timeline.getFromYear(),0,1)).getTime(),f=((new Date).getTime()-f)/315576E5,g=a/f;if(this.tickSvgs)for(var k in this.tickSvgs)this.masterSvg.removeChild(this.tickSvgs[k]);this.tickSvgs=[];for(k=0;k<f;k+=1){var e=b+a-k*g,h=document.createElementNS("http://www.w3.org/2000/svg","line");h.setAttribute("x1",c-8);h.setAttribute("y1",
e);h.setAttribute("x2",c+8);h.setAttribute("y2",e);h.setAttribute("style","stroke:rgb(0,0,0);stroke-width:2");this.tickSvgs.push(h);this.masterSvg.appendChild(h);d&&0!=k&&(h=document.createElementNS("http://www.w3.org/2000/svg","text"),h.setAttribute("x",c+3),h.setAttribute("y",e+10),h.setAttribute("fill","black"),h.setAttribute("font-size","10"),e=document.createTextNode(this.timeline.getFromYear()+k+""),h.appendChild(e),this.tickSvgs.push(h),this.masterSvg.appendChild(h))}};
VisualisationVerticalSmallBar.prototype.updateStartYearAndNowString=function(){if(this.labelSvgs)for(var a in this.labelSvgs)this.masterSvg.removeChild(this.labelSvgs[a]);this.labelSvgs=[];a=this.getCenter()-15;var b=this.config.scale.fontSize,d=0.3*b;if(this.config.drawToday){var c=document.createElementNS("http://www.w3.org/2000/svg","text");c.setAttribute("x",a);c.setAttribute("y",2.7*d);c.setAttribute("fill","black");c.setAttribute("font-size",b);var f=document.createTextNode("today");c.appendChild(f);
this.labelSvgs.push(c);this.masterSvg.appendChild(c)}this.config.drawBaseLineYear&&(c=document.createElementNS("http://www.w3.org/2000/svg","text"),c.setAttribute("x",a),c.setAttribute("y",this.getHeight()-d),c.setAttribute("fill","black"),c.setAttribute("font-size",b),a=document.createTextNode(this.timeline.getFromYear()),c.appendChild(a),this.labelSvgs.push(c),this.masterSvg.appendChild(c))};
VisualisationVerticalSmallBar.prototype.updateArrowHead=function(){var a=this.getTopOffsetForScale(),b=this.config.scale.lineWidth,d=this.getCenter(),c=this.config.scale.arrowHeadWidth/2,f=this.config.scale.arrowHeadHeight,g=d-c,k=f+a,c=d+c,f=f+a;this.arrowHeadSvg||(this.arrowHeadSvg=document.createElementNS("http://www.w3.org/2000/svg","polyline"),this.masterSvg.appendChild(this.arrowHeadSvg));this.arrowHeadSvg.setAttribute("points",g+","+k+" "+d+","+a+" "+c+","+f);this.arrowHeadSvg.setAttribute("style",
"fill:none;stroke:black;stroke-width:"+b)};VisualisationVerticalSmallBar.prototype.updateEntries=function(){var a=this.timeline.getTimelineEntries(),b;for(b in this.timelineEntryVisualisationMaps)this.masterSvg.removeChild(this.timelineEntryVisualisationMaps[b]);for(b in a)this.onNewTimelineEntry(a[b])};
VisualisationVerticalSmallBar.prototype.getShapeForTimelineEntry=function(a){var b=this.timeline.getTakenLevelsInTimeRange(a.fromDate,a.toDate,a);a.color||(a.color=this.getNextColor());for(var d=a.color,c=0;-1!=b.indexOf(c);)c+=1;a.level=c;var f=this.getPosForDate(a.fromDate),b=this.getPosForDate(a.toDate),f=f-b;this.getWidth();var g=this.getCenter(),k=0,k=0==c%2?g+1+6*(c/2):g-7-6*((c-1)/2),e=document.createElementNS("http://www.w3.org/2000/svg","rect");e.setAttribute("y",b);e.setAttribute("x",k);
e.setAttribute("height",f);e.setAttribute("width",5);e.setAttribute("style","fill:#"+d+";stroke:black;stroke-width:1;pointer-events:all;");e.setAttribute("class","js_timeline_entry");e.onmouseover=function(b){a.tooltip=new Tooltip(b,a.title);e.classList.add("hover")};e.onmouseout=function(){a.tooltip.destroyExistingTooltip();e.classList.remove("hover")};return e};function addEvent(a,b,d){a.addEventListener?a.addEventListener(b,d,!1):a.attachEvent&&a.attachEvent("on"+b,d)};
