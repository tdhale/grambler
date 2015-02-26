package com.simplybest.grambler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

@WebServlet(name = "grambler", urlPatterns =
{
	"/grambler/*",
})
public class grambler extends HttpServlet implements java.io.Serializable
{
	private static final String CONTENT_TYPE = "text/plain";
	final static Charset ENCODING = StandardCharsets.UTF_8;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(CONTENT_TYPE);
        response.addHeader("Access-Control-Allow-Origin", "*");
        //response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        //response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        //response.addHeader("Access-Control-Max-Age", "1728000");

		PrintWriter out = null;
		try
		{
			out = response.getWriter();
			out.print("{\"Anagrams\":");
			String word = StringUtils.trimToEmpty(request.getParameter("w"));
			if (word.isEmpty())
			{
				out.print("[{\"word\":\"Usage: grambler?w=sampleword\"}]");
			}
			else
			{
				String fullPath = getServletConfig().getServletContext().getRealPath("/WEB-INF/words.txt");
				List<String> anagramsList = findMatch(word, fullPath);

				if (anagramsList.size() == 0)
				{
					out.print("[{\"word\":\"word not found from " + word + "\"}]");
				}
				else
				{
					out.print("[{\"word\":\"" + StringUtils.join(anagramsList, "\"},{\"word\":\"") + "\"}]");
					//for (String l : anagramsList) out.println(l);
				}
			}
			out.println("}");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (out != null) out.close();
		}
	}

	public static List<String> readSmallTextFile(String aFileName) throws IOException
	{
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, ENCODING);
	}

	public static List<List<String>> find(String fileName) throws IOException
	{
		//Map<String, List<String>> tempListOfAnagrams = new HashMap<>();
		Map<String, List<String>> tempListOfAnagrams = new HashMap<String, List<String>>();
		List<String> listOfWords = readSmallTextFile(fileName);
		for (String s : listOfWords)
		{
			char[] c = s.toUpperCase().toCharArray();
			Arrays.sort(c);
			List<String> l = tempListOfAnagrams.get(String.valueOf(c));
			if (l == null)
			{
				l = new ArrayList<String>();
			}
			l.add(s);
			tempListOfAnagrams.put(String.valueOf(c), l);
		}
		List<List<String>> anagrams = new ArrayList<List<String>>();
		for (Map.Entry<String, List<String>> e : tempListOfAnagrams.entrySet())
		{
			if (e.getValue().size() > 1)
			{
				anagrams.add(e.getValue());
			}
		}
		return anagrams;
	}

	public static List<String> findMatch(String word, String fileName) throws IOException
	{
		char[] wordVal = word.toUpperCase().toCharArray();
		Arrays.sort(wordVal);

		List<String> listOfWords = readSmallTextFile(fileName);
		List<String> anagrams = new ArrayList<String>();
		for (String s : listOfWords)
		{
			char[] c = s.toUpperCase().toCharArray();
			Arrays.sort(c);
			if (Arrays.equals(wordVal, c))
			{
				anagrams.add(s);
			}
		}
		return anagrams;
	}

}
