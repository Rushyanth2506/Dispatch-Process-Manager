import com.sun.net.httpserver.*;
import java.net.*;import java.nio.file.*;import java.nio.charset.StandardCharsets;import java.io.*;import java.util.*;import java.util.regex.*;
public class Main {
 static final Path ROOT=Path.of("frontend").toAbsolutePath().normalize(),DATA=Path.of("data.json");
 static void send(HttpExchange x,int code,String type,String s)throws IOException{byte[] b=s.getBytes(StandardCharsets.UTF_8);x.getResponseHeaders().set("Content-Type",type+"; charset=utf-8");x.getResponseHeaders().set("X-Content-Type-Options","nosniff");x.sendResponseHeaders(code,b.length);try(var o=x.getResponseBody()){o.write(b);}}
 static String body(HttpExchange x)throws IOException{byte[] b=x.getRequestBody().readNBytes(65537);if(b.length>65536)throw new IllegalArgumentException("Request too large.");return new String(b,StandardCharsets.UTF_8);}
 static String validate(String s){s=s.replaceAll("\\s","");if(s.equals("[]"))return s;var m=Pattern.compile("\\{\"pid\":(\\d+),\"at\":(\\d+),\"bt\":(\\d+),\"pr\":(\\d+)\\}").matcher(s);var rows=new ArrayList<String>();var objects=new ArrayList<String>();while(m.find()){rows.add(m.group(1)+","+m.group(2)+","+m.group(3)+","+m.group(4));objects.add(m.group());}if(!s.equals("["+String.join(",",objects)+"]"))throw new IllegalArgumentException("Invalid process JSON.");Scheduler.parse(String.join(";",rows));return s;}
 public static void main(String[] args)throws Exception{
 var server=HttpServer.create(new InetSocketAddress("127.0.0.1",8080),0);
 server.createContext("/",x->{try{String path=x.getRequestURI().getPath(),method=x.getRequestMethod();
 if(path.equals("/api/schedule")){if(!method.equals("POST")){send(x,405,"text/plain","POST required");return;}String[] parts=body(x).split(";",2),opts=parts[0].split(",");if(parts.length!=2||opts.length!=2)throw new IllegalArgumentException("Invalid request.");send(x,200,"application/json",Scheduler.run(opts[0],Integer.parseInt(opts[1]),Scheduler.parse(parts[1])));}
 else if(path.equals("/api/processes")){if(method.equals("GET"))send(x,200,"application/json",Files.exists(DATA)?Files.readString(DATA):"[]");else if(method.equals("POST")){String s=validate(body(x));Files.writeString(Path.of("data.json.tmp"),s);Files.move(Path.of("data.json.tmp"),DATA,StandardCopyOption.REPLACE_EXISTING);send(x,200,"application/json","{\"saved\":true}");}else send(x,405,"text/plain","GET or POST required");}
 else {if(!method.equals("GET")){send(x,405,"text/plain","GET required");return;}Path f=ROOT.resolve(path.equals("/")?"index.html":path.substring(1)).normalize();if(!f.startsWith(ROOT)||!Files.isRegularFile(f)){send(x,404,"text/plain","Not found");return;}send(x,200,path.endsWith(".css")?"text/css":path.endsWith(".js")?"text/javascript":"text/html",Files.readString(f));}
 }catch(IllegalArgumentException e){send(x,400,"text/plain","Invalid input: "+e.getMessage());}catch(Exception e){send(x,500,"text/plain","Unable to complete request.");}});
 server.start();System.out.println("Open http://localhost:8080");
 }
}
