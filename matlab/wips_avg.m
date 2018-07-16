function [avg_wips] = wips_avg(dat)
%------------------------------------------------------------------------
% function [avg_wips] = wips(dat)
%
% Compute average throughput over measurement interval.
%------------------------------------------------------------------------

s = (dat.startMI-dat.startRU)/1000;
e = (dat.startRD-dat.startRU)/1000;

s=floor(s);
e=ceil(e);

avg_wips = sum(dat.wips(s:e))/(e-s+1);
