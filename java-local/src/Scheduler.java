import java.util.*;
public class Scheduler {
 record P(int pid,int at,int bt,int pr){}
 static List<P> parse(String s){var p=new ArrayList<P>();var ids=new HashSet<Integer>();for(String row:s.split(";")){var v=row.split(",");if(v.length!=4)throw new IllegalArgumentException("Four fields required.");int id=Integer.parseInt(v[0]),a=Integer.parseInt(v[1]),b=Integer.parseInt(v[2]),r=Integer.parseInt(v[3]);if(id<1||!ids.add(id)||a<0||a>100000||b<1||b>10000||r<0||r>100000)throw new IllegalArgumentException("Check unique IDs and numeric ranges.");p.add(new P(id,a,b,r));}if(p.isEmpty()||p.size()>100)throw new IllegalArgumentException("Use 1–100 processes.");return p;}
 static String run(String algo,int q,List<P> p){
 if(!Set.of("FCFS","SJF","PRIORITY","RR").contains(algo)||q<1||q>10000)throw new IllegalArgumentException("Invalid algorithm or quantum.");
 int n=p.size(),t=0,done=0,busy=0,next=0;int[] rem=new int[n],ct=new int[n],first=new int[n];Arrays.fill(first,-1);
 var order=new ArrayList<Integer>();for(int i=0;i<n;i++){rem[i]=p.get(i).bt;order.add(i);}order.sort(Comparator.comparingInt((Integer i)->p.get(i).at).thenComparingInt(i->i));
 var queue=new ArrayDeque<Integer>();var g=new ArrayList<String>();
 while(done<n){int k=-1;
 if(algo.equals("RR")){while(next<n&&p.get(order.get(next)).at<=t)queue.add(order.get(next++));if(!queue.isEmpty())k=queue.remove();}
 else for(int i:order)if(rem[i]>0&&p.get(i).at<=t){if(k<0)k=i;else if(algo.equals("SJF")&&p.get(i).bt<p.get(k).bt||algo.equals("PRIORITY")&&p.get(i).pr<p.get(k).pr)k=i;}
 if(k<0){int end=Integer.MAX_VALUE;for(int i=0;i<n;i++)if(rem[i]>0)end=Math.min(end,p.get(i).at);g.add("{\"pid\":0,\"start\":"+t+",\"end\":"+end+"}");t=end;continue;}
 if(first[k]<0)first[k]=t;int start=t,d=algo.equals("RR")?Math.min(q,rem[k]):rem[k];t+=d;busy+=d;rem[k]-=d;
 g.add("{\"pid\":"+p.get(k).pid+",\"start\":"+start+",\"end\":"+t+"}");
 if(rem[k]==0){ct[k]=t;done++;}if(algo.equals("RR")){while(next<n&&p.get(order.get(next)).at<=t)queue.add(order.get(next++));if(rem[k]>0)queue.add(k);}
 if(g.size()>100000)throw new IllegalArgumentException("Increase quantum to reduce time slices.");
 }
 double wt=0,tat=0,rt=0;var rows=new ArrayList<String>();for(int i=0;i<n;i++){P x=p.get(i);int a=ct[i]-x.at,w=a-x.bt,r=first[i]-x.at;wt+=w;tat+=a;rt+=r;rows.add("{\"pid\":"+x.pid+",\"ct\":"+ct[i]+",\"tat\":"+a+",\"wt\":"+w+",\"rt\":"+r+"}");}
 return "{\"gantt\":["+String.join(",",g)+"],\"processes\":["+String.join(",",rows)+"],\"avgWT\":"+wt/n+",\"avgTAT\":"+tat/n+",\"avgRT\":"+rt/n+",\"utilization\":"+100.0*busy/t+",\"throughput\":"+(double)n/t+",\"totalTime\":"+t+"}";
 }
}
