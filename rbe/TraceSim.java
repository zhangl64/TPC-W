package rbe;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class TraceSim
{
    private long times[];
    private int ebCounts[];

    public TraceSim(String fname, double traceSpeed, int numEbs)
    throws Exception {
        BufferedReader traceRd =
            new BufferedReader(new FileReader(fname));
        List<Long> timeList = new ArrayList<Long>();
        List<Double> loadList = new ArrayList<Double>();
        double maxLoad = 0.0;
        try {
            long prevTraceSecs = -1;
            String line;
            while ((line = traceRd.readLine()) != null) {
                int commentPos = line.indexOf('#');
                if (commentPos != -1) {
                    line = line.substring(0, commentPos);
                }
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                String[] fields = line.split("\\s+");
                if (fields.length == 2) {
                    long traceSecs = Long.parseLong(fields[0]);
                    if (traceSecs < 0) {
                        throw new Exception("Negative time " + traceSecs +
                                            " in trace " + fname);
                    }
                    if (traceSecs <= prevTraceSecs) {
                        throw new Exception("Out-of-order time " + traceSecs +
                                            " in trace " + fname);
                    }
                    prevTraceSecs = traceSecs;
                    long simMillis = (long) (traceSecs * 1000 / traceSpeed);
                    timeList.add(new Long(simMillis));
                    double traceLoad = Double.parseDouble(fields[1]);
                    if (traceLoad < 0.0) {
                        throw new Exception("Negative load " + traceLoad +
                                            " in trace " + fname);
                    }
                    loadList.add(new Double(traceLoad));
                    if (traceLoad > maxLoad) {
                        maxLoad = traceLoad;
                    }
                } else {
                    throw new Exception("Invalid line in trace " + 
                                        fname + ":\n" + line);
                }
            }
        } finally {
            traceRd.close();
        }
        assert timeList.size() == loadList.size();
        if (timeList.size() < 2) {
            throw new Exception("Empty or too short trace: " + fname);
        }
        if (maxLoad == 0.0) {
            maxLoad = 1.0;
        }
        assert maxLoad > 0.0;
        times = new long[timeList.size()];
        ebCounts = new int[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            times[i] = timeList.get(i).longValue();
            double relativeLoad = loadList.get(i).doubleValue() / maxLoad;
            ebCounts[i] = (int) Math.round(relativeLoad * numEbs);
        }
    }

    public int getInitEBCount() {
        return ebCounts[0];
    }

    public void run(Vector<EB> ebs, long start, long term)
    throws InterruptedException {
        final long period = times[times.length - 1] - times[times.length - 2];
        final long traceDuration = times[times.length - 1] + period - times[0];
        long timeOffset = start;
        int prevCount = ebCounts[0];
        while (true) {
            for (int i = 0; i < times.length; i++) {
                long t = times[i] + timeOffset;
                int count = ebCounts[i];
                RBE.rampEBs(ebs, prevCount, count, t, term);
                if (t >= term) {
                    return;
                }
                prevCount = count;
            }
            timeOffset += traceDuration;
        }
    }

    public static void main(String args[]) throws Exception {
        String fname = args[0];
        double traceSpeed = Double.parseDouble(args[1]);
        int numEbs = Integer.parseInt(args[2]);
        TraceSim t = new TraceSim(fname, traceSpeed, numEbs);
        for (int i = 0; i < t.times.length; i++) {
            System.out.println("ebs(" + t.times[i] + ") = " + t.ebCounts[i]);
        }
    }
}
