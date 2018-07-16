function [ avg ] = wirt_avg(dat)
%WIRT_AVG Calculates average web interaction response time
%   Reads per-interaction averages from dat and performs weighted
%   averaging.

samples = 0;
sum = 0;
for i=1:length(dat.wirt)
    samples = samples + dat.wirt{i}.samples;
    sum = sum + dat.wirt{i}.sum;
end

avg = sum / samples;
