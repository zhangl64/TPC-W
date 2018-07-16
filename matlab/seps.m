function [w] = seps(dat)
%------------------------------------------------------------------------
% function [] = seps(dat)
%
% Plot server errors per second, SEPS, over time.
% Compute and plot average SEPS over measurement interval.
%------------------------------------------------------------------------

% Find end of data.

for i=1:length(dat.wips)
  j = length(dat.wips)-i+1;
  if (dat.wips(j)>0) 
    break;
  end
end

len = j;

clf;
hold on;
l = plot(dat.seps(1:len), 'r.');

avg(1) = 0;
for i=1:30
  avg(1) = avg(1)  +dat.seps(i);
end

for i=31:len
  avg(i-29) = avg(i-30) + dat.seps(i) - dat.seps(i-30);
end

avg = avg / 30;

l = plot((30:len)-15, avg, 'k-');

ax = axis;
s = (dat.startMI-dat.startRU)/1000;
e = (dat.startRD-dat.startRU)/1000;
plot([s,s], ax(3:4), 'b--');
plot([e,e], ax(3:4), 'b--');

s=floor(s);
e=ceil(e);

avg = sum(dat.seps(s:e))/(e-s+1);
plot([s,e], [avg, avg], 'g--');

title({'Server Errors Over Time', sprintf('Avg SEPS = %9.2f', avg)});
ylabel('SEPS');
xlabel('Time (s)');
