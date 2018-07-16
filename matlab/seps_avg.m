function [avg_seps] = seps_avg(dat)
%------------------------------------------------------------------------
% Compute average server error rate over measurement interval.
%------------------------------------------------------------------------

s = (dat.startMI-dat.startRU)/1000;
e = (dat.startRD-dat.startRU)/1000;

s=floor(s);
e=ceil(e);

avg_seps = sum(dat.seps(s:e))/(e-s+1);
