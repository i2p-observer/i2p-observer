package ch.bfh.mullj10.i2pobserver.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ObserverProperties {

	// folder setting
	private final String USER_FOLDER = "/home/user/";
	
	// settings for Observer
	private final String WORKING_DIR = USER_FOLDER + "i2p-observer/";
	private final int RUNS_PER_DAY = 24;
	
	// settings for RouterInfoAnalyzer
	private final String GEOIP_DIR = USER_FOLDER + "i2p-observer/";
	
	// settings for NetDBImporter
	private String netDBFolder = USER_FOLDER + ".i2p/netDb";
	
	// settings for HtmlOutput
	private final String DOCUMENTPATH = USER_FOLDER + "i2p-observer";
	private final String PAGENAME_DAILY_OVERVIEW = "overview_daily.html";
	private final String PAGENAME_MONTHLY_OVERVIEW = "overview_monthly.html";
	private final String PAGE_TOP = "<!DOCTYPE html>\n<html lang=\"de\">\n<head>\n<title>I2P Observer - Statistics</title>\n<meta charset=\"utf-8\" />\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n<meta name=\"description\" content=\"I2P Observer\">\n<meta name=\"author\" content=\"security4web.ch/i2pobserver\">\n<!-- Bootstrap core CSS -->\n<link href=\"../theme/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n\n<!-- Custom styles for this template -->\n<link href=\"../theme/bootstrap/navbar.css\" rel=\"stylesheet\">\n<link href=\"../theme/bootstrap/font.css\" rel=\"stylesheet\">\n<link href=\"../theme/bootstrap/margin.css\" rel=\"stylesheet\">\n<link href=\"../theme/bootstrap/textarea.css\" rel=\"stylesheet\">\n<link href=\"../theme/bootstrap/agenda.css\" rel=\"stylesheet\">\n<link href=\"../theme/font-awesome/css/font-awesome.min.css\" rel=\"stylesheet\">\n<style> table, td, th { border: 1px solid black; } </style>\n</head>\n\n<body>\n<div class=\"container\">\n\n<!-- Static navbar -->\n\n<div class=\"navbar navbar-default navbar-fixed-top\" role=\"navigation\">\n<div class=\"container\">\n<div class=\"navbar-header\">\n<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\n<span class=\"sr-only\">Toggle navigation</span>\n<span class=\"icon-bar\"></span>\n<span class=\"icon-bar\"></span>\n<span class=\"icon-bar\"></span>\n</button>\n<a class=\"navbar-brand\" href=\"/../\">\n<b>I2P Observer</b></a>\n</div>\n<div class=\"navbar-collapse collapse\">\n<ul class=\"nav navbar-nav navbar-right\">\n<li><a href=\"../index.html\">Home</a></li>\n<li><a href=\"../about.html\">About</a></li>\n<li><a href=\"overview_daily.html\">Daily Statistics</a></li>\n<li><a href=\"overview_monthly.html\">Monthly Statistics</a></li>\n<li class=\"dropdown\">\n<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Archive<b class=\"caret\"></b></a>\n<ul class=\"dropdown-menu sub-menu\">\n<li><a href=\"archive_last_month.html\">Last Month</a></li>\n<li><a href=\"archive_last_year.html\">Last Year</a></li>\n</ul>\n</li>\n<li><a href=\"../help.html\">Help</a></li>\n<li><a href=\"../impressum.html\">Impressum</a></li>\n</ul>\n</div><!--/.nav-collapse -->\n</div><!--/.container-fluid -->\n</div>\n<section id=\"content\" class=\"body\">\n<div class=\"row\">\n<div class=\"col-md-9\">\n<header>\n</header>\n\n";
	private final String PAGE_BOTTOM = "</div> <!-- /container -->\n\n\n<!-- Bootstrap core JavaScript\n================================================== -->\n<!-- Placed at the end of the document so the pages load faster -->\n<script src=\"../theme/jquery/jquery.min.js\"></script>\n<script src=\"../theme/bootstrap/js/bootstrap.min.js\"></script>\n<script src=\"../theme/bootstrap/headings.js\"></script>\n<script src=\"../theme/bootstrap/table.js\"></script>\n";

	// settings for HtmlUploader
	private final String FTP_HOSTNAME = "security4web.ch";
	private final String FTP_USERNAME = "Mueller%40security4web.ch";
	private final String FTP_PASSWORD = "Tor4I2P-2016";
	
	// uniform names in RouterInfoStatistic
	private final String FLOODFIL_ROUTERS_DENOTATION = "floodfilRouters";
	private final String LEASESETS_DENOTATION = "leaseSets";
	private final String KNOWN_ROUTERS_DENOTATION = "knownRouters";

	// uniform parameters
	private DateFormat hourlyDateFormat = new SimpleDateFormat("HH:mm");
	private DateFormat dailyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat monthlyDateFormat = new SimpleDateFormat("yyyy-MM");
	
	public String getWORKING_DIR() {
		return WORKING_DIR;
	}	
	public int getRUNS_PER_DAY(){
		return RUNS_PER_DAY;
	}
	public String getGEOIP_DIR(){
		return GEOIP_DIR;
	}
	public String getNetDBFolderPath() {
		return netDBFolder;
	}
	public String getDOCUMENTPATH() {
		return DOCUMENTPATH;
	}
	public String getPAGENAME_DAILY_OVERVIEW(){
		return PAGENAME_DAILY_OVERVIEW;
	}
	public String getPAGENAME_MONTHLY_OVERVIEW(){
		return PAGENAME_MONTHLY_OVERVIEW;
	}
	public String getPAGE_TOP(){
		return PAGE_TOP;
	}
	public String getPAGE_BOTTOM(){
		return PAGE_BOTTOM;
	}
	public String getFTP_HOSTNAME(){
		return FTP_HOSTNAME;
	}
	public String getFTP_USERNAME(){
		return FTP_USERNAME;
	}
	public String getFTP_PASSWORD(){
		return FTP_PASSWORD;
	}
	public String getFLOODFIL_ROUTERS_DENOTATION() {
		return FLOODFIL_ROUTERS_DENOTATION;
	}
	public String getLEASESETS_DENOTATION() {
		return LEASESETS_DENOTATION;
	}
	public String getKNOWN_ROUTERS_DENOTATION() {
		return KNOWN_ROUTERS_DENOTATION;
	}
	public String getHourlyDate() {
		return hourlyDateFormat.format(Calendar.getInstance().getTime());
	}
	public String getDailyDate() {
		return dailyDateFormat.format(Calendar.getInstance().getTime());
	}
	public String getMonthlyDate() {
		return monthlyDateFormat.format(Calendar.getInstance().getTime());
	}	
}
