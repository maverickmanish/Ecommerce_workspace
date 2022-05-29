package com.shopme.admin.Exporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

public class UserCsvExporter extends AbstractExporter {

	public class CategoryCsvExporter {

	}

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException
	{
		super.setResponseHeader(response,"test/csv",".csv", "users_");
	
	ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(),
			CsvPreference.STANDARD_PREFERENCE); 
	
	String[] csvHeader = {"User ID","E-mail","First Name","Last Name","Roles",
			"Enabled"};
	String[] fieldMapping = {"id","email","firstName","lastName","roles",
	"enabled"};
	
	beanWriter.writeHeader(csvHeader);
	
	for (User user : listUsers) {
		beanWriter.write(user, fieldMapping);
	}
	beanWriter.close();
	}
}
