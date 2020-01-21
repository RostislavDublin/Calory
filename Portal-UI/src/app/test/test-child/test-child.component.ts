import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {TestDataContainer} from '../test-parent/test-parent.component';

@Component({
  selector: 'app-test-child',
  templateUrl: './test-child.component.html',
  styleUrls: ['./test-child.component.css']
})
export class TestChildComponent implements OnInit, OnChanges {

  @Input() dataInChild: TestDataContainer;

  constructor() {
  }

  ngOnInit() {
    console.log('Child list onInit');
  }
  ngOnChanges() {
    console.log('Child list onChange');
  }

}
