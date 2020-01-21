import {Component, OnInit} from '@angular/core';
import {CrudListConfigDefault} from '../config/crud-list-config-default';
import {CrudListConfig} from '../config/crud-list-config';

@Component({
  selector: 'app-crud-list-example',
  templateUrl: './crud-list-example.component.html',
  styleUrls: ['./crud-list-example.component.css']
})
export class CrudListExampleComponent implements OnInit {

  config: CrudListConfig = new CrudListConfigDefault();

  constructor() {
  }

  ngOnInit() {
  }

}
