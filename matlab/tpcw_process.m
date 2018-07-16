function [] = tpcw_process(inputpath, input)

path(inputpath, path);

try
  data = feval(input);

  output = sprintf('%s.proc', input);
  fid = fopen(output, 'w');
  fprintf(fid, 'StartMillis,EndMillis,WIRT,WIDMR,WIPS,SEPS\n');
  fprintf(fid, '%.0f,%.0f,%.0f,%.3f,%.3f,%.3f\n', ...
          data.startMI, data.startRD, ...
          wirt_avg(data), widmr(data), wips_avg(data), seps_avg(data));

  wips_output = sprintf('%s_wips.eps', input);
  wips(data);
  saveas(gcf, wips_output, 'epsc2');

  seps_output = sprintf('%s_seps.eps', input);
  seps(data);
  saveas(gcf, seps_output, 'epsc2');

  wirt_output = sprintf('%s_wirt.eps', input);
  wirt(data);
  saveas(gcf, wirt_output, 'epsc2');
catch
  err = lasterror;
  st1 = err.stack(1,1);
  fprintf(2, 'Could not process %s: %s\n  at %s line %d\n', ...
          input, err.message, st1.file, st1.line);
end
fclose(fid);
close;
